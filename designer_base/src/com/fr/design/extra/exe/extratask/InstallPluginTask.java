package com.fr.design.extra.exe.extratask;


import com.fr.design.extra.exe.callback.InstallOnlineCallback;
import com.fr.design.extra.exe.callback.JSCallback;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;


/**
 * Created by ibm on 2017/5/27.
 */
public class InstallPluginTask extends AbstractExtraPluginTask {
    protected JSCallback jsCallback;


    public InstallPluginTask(PluginMarker pluginMarker, JSCallback jsCallback) {
        this.pluginMarker = pluginMarker;
        this.jsCallback = jsCallback;
    }

    @Override
    public void doExtraPluginTask() {
        PluginManager.getController().install(pluginMarker, new InstallOnlineCallback(pluginMarker, jsCallback));
    }
}
