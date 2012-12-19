/**
 * 
 */
package org.iplantc.de.client.collaborators.util;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.collaborators.models.Collaborator;
import org.iplantc.de.client.collaborators.models.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.collaborators.models.CollaboratorsList;
import org.iplantc.de.client.events.CollaboratorsLoadedEvent;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.utils.NotifyInfo;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;

/**
 * @author sriram
 * 
 */
public class CollaboratorsUtil {

    private static List<Collaborator> currentCollaborators;
    private static List<Collaborator> searchResutls;
    private static CollaboratorAutoBeanFactory factory = GWT.create(CollaboratorAutoBeanFactory.class);

    private static List<Collaborator> parseResults(String result) {
        AutoBean<CollaboratorsList> bean = AutoBeanCodex
                .decode(factory, CollaboratorsList.class, result);
        JSONObject obj = JsonUtil.getObject(result);
        boolean truncated = JsonUtil.getBoolean(obj, "truncated", false);
        if (truncated) {
            AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.searchCollab(),
                    I18N.DISPLAY.collaboratorSearchTruncated());
        }
        return bean.as().getCollaborators();
    }

    /**
     * @return the currentCollaborators
     */
    public static List<Collaborator> getCurrentCollaborators() {
        return currentCollaborators;
    }

    /**
     * @param currentCollaborators the currentCollaborators to set
     */
    public static void setCurrentCollaborators(List<Collaborator> currentCollaborators) {
        CollaboratorsUtil.currentCollaborators = currentCollaborators;
    }

    public static void getCollaborators(final AsyncCallback<Void> superCallback) {
        if (getCurrentCollaborators() == null) {
            UserSessionServiceFacade facade = new UserSessionServiceFacade();
            facade.getCollaborators(new GetCollaboratorsCallback(superCallback));
        } else {
            superCallback.onSuccess(null);
        }
    }

    public static void search(final String term, final AsyncCallback<Void> superCallback) {
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        facade.searchCollaborators(term, new SearchCallback(superCallback));
    }

    public static boolean checkCurrentUser(Collaborator model) {
        if (model.getUserName().equalsIgnoreCase(UserInfo.getInstance().getUsername())) {
            return true;
        }

        return false;
    }

    /**
     * @return the searchResutls
     */
    public static List<Collaborator> getSearchResutls() {
        return searchResutls;
    }

    public static Collaborator findCollaboratorByUserName(String userName) {
        List<Collaborator> collabs = getCurrentCollaborators();
        for (Collaborator c : collabs) {
            if (c.getUserName().equals(userName)) {
                return c;
            }
        }
        // TODO: must return dummy collaborator object here
        return null;
    }

    public static boolean isCollaborator(Collaborator c) {
        return getCurrentCollaborators().contains(c);
    }

    /**
     * @param searchResutls the searchResutls to set
     */
    public static void setSearchResutls(List<Collaborator> searchResutls) {
        CollaboratorsUtil.searchResutls = searchResutls;
    }

    private static final class GetCollaboratorsCallback implements AsyncCallback<String> {
        private final AsyncCallback<Void> callback;

        public GetCollaboratorsCallback(final AsyncCallback<Void> callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
            if (callback != null) {
                callback.onFailure(caught);
            }
        }

        @Override
        public void onSuccess(String result) {
            setCurrentCollaborators(parseResults(result));
            CollaboratorsLoadedEvent event = new CollaboratorsLoadedEvent();
            EventBus.getInstance().fireEvent(event);
            if (callback != null) {
                callback.onSuccess(null);
            }
        }
    }

    public static boolean isCurrentCollaborator(Collaborator c) {
        for (Collaborator current : getCurrentCollaborators()) {
            if (current.getId().equals(c.getId())) {
                return true;
            }
        }

        return false;
    }

    public static void addCollaborators(final List<Collaborator> models,
            final AsyncCallback<Void> supercallback) {
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        JSONObject obj = buildJSONModel(models);
        facade.addCollaborators(obj, new AddCollaboratorCallback(models, supercallback));
    }

    public static void removeCollaborators(final List<Collaborator> models,
            final AsyncCallback<Void> supercallback) {
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        JSONObject obj = buildJSONModel(models);
        facade.removeCollaborators(obj, new RemoveCollaboratorCallback(models, supercallback));
    }

    private static JSONObject buildJSONModel(final List<Collaborator> models) {
        JSONArray arr = new JSONArray();
        int count = 0;
        for (Collaborator model : models) {
            JSONObject user = new JSONObject();
            user.put("username", new JSONString(model.getUserName()));
            arr.set(count++, user);
        }

        JSONObject obj = new JSONObject();
        obj.put("users", arr);
        return obj;
    }

    private static final class SearchCallback implements AsyncCallback<String> {

        private final AsyncCallback<Void> callback;

        public SearchCallback(final AsyncCallback<Void> callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
            if (callback != null) {
                callback.onFailure(caught);
            }
        }

        @Override
        public void onSuccess(String result) {
            setSearchResutls(parseResults(result));
            if (callback != null) {
                callback.onSuccess(null);
            }
        }
    }

    private static final class AddCollaboratorCallback implements AsyncCallback<String> {
        private final AsyncCallback<Void> callback;
        private final List<Collaborator> models;

        public AddCollaboratorCallback(final List<Collaborator> models,
                final AsyncCallback<Void> callback) {
            this.callback = callback;
            this.models = models;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.addCollabErrorMsg(), caught);
            if (callback != null) {
                callback.onFailure(caught);
            }

        }

        @Override
        public void onSuccess(String result) {
            getCurrentCollaborators().addAll(models);
            StringBuilder builder = new StringBuilder();
            for (Collaborator c : models) {
                builder.append(c.getUserName() + ",");
            }

            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }

            NotifyInfo.display(I18N.DISPLAY.collaboratorAdded(),
                    I18N.DISPLAY.collaboratorAddConfirm(builder.toString()));
            if (callback != null) {
                callback.onSuccess(null);
            }
        }
    }

    private static final class RemoveCollaboratorCallback implements AsyncCallback<String> {
        private final AsyncCallback<Void> callback;
        private final List<Collaborator> models;

        public RemoveCollaboratorCallback(final List<Collaborator> models,
                final AsyncCallback<Void> callback) {
            this.callback = callback;
            this.models = models;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.removeCollabErrorMsg(), caught);
            if (callback != null) {
                callback.onFailure(caught);
            }

        }

        @Override
        public void onSuccess(String result) {
            StringBuilder builder = new StringBuilder();
            for (Collaborator c : models) {
                builder.append(c.getUserName() + ",");
                getCurrentCollaborators().remove(c);

            }

            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }

            NotifyInfo.display(I18N.DISPLAY.collaboratorRemoved(),
                    I18N.DISPLAY.collaboratorRemoveConfirm(builder.toString()));
            if (callback != null) {
                callback.onSuccess(null);
            }

        }
    }

}