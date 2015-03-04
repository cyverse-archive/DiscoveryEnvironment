package org.iplantc.de.apps.client.events.selection;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by jstroot on 2/23/15.
 *
 * @author jstroot
 */
public class AppRatingDeselected extends GwtEvent<AppRatingDeselected.AppRatingDeselectedHandler> {
    public static interface AppRatingDeselectedHandler extends EventHandler {
        void onAppRatingDeselected(AppRatingDeselected event);
    }

    public static interface HasAppRatingDeselectedHandlers {
        HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselectedHandler handler);
    }

    public static Type<AppRatingDeselectedHandler> TYPE = new Type<>();
    private final App app;

    public AppRatingDeselected(final App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public Type<AppRatingDeselectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AppRatingDeselectedHandler handler) {
        handler.onAppRatingDeselected(this);
    }
}