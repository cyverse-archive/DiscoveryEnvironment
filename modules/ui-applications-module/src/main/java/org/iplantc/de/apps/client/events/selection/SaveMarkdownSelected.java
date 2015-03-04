package org.iplantc.de.apps.client.events.selection;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import com.sencha.gxt.widget.core.client.Component;

/**
 * Created by jstroot on 3/4/15.
 *
 * @author jstroot
 */
public class SaveMarkdownSelected extends GwtEvent<SaveMarkdownSelected.SaveMarkdownSelectedHandler> {
    public static interface HasSaveMarkdownSelectedHandlers {
        HandlerRegistration addSaveMarkdownSelectedHandler(SaveMarkdownSelectedHandler handler);
    }

    public static interface SaveMarkdownSelectedHandler extends EventHandler {
        void onSaveMarkdownSelected(SaveMarkdownSelected event);
    }

    public static Type<SaveMarkdownSelectedHandler> TYPE = new Type<>();
    private final App app;
    private final String editorContent;

    /**
     * @param app           the app whose documentation is to be saved.
     * @param editorContent the unaltered editor content.
     */
    public SaveMarkdownSelected(final App app,
                                final String editorContent) {
        this.app = app;
        this.editorContent = editorContent;
    }

    public App getApp() {
        return app;
    }

    public Type<SaveMarkdownSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public String getEditorContent() {
        return editorContent;
    }

    @Override
    public Component getSource() {
        return (Component) super.getSource();
    }

    protected void dispatch(SaveMarkdownSelectedHandler handler) {
        handler.onSaveMarkdownSelected(this);
    }
}