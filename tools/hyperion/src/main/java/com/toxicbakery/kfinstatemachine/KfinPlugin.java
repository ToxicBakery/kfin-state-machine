package com.toxicbakery.kfinstatemachine;

import android.support.annotation.Nullable;
import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.HashMap;
import java.util.Map;

@AutoService(Plugin.class)
public class KfinPlugin extends Plugin {

    static final Map<String, IStateMachine<?>> registeredMachines = new HashMap<>();

    public static void registerMachine(String id, IStateMachine<?> machine) {
        registeredMachines.put(id, machine);
    }

    public static void unregisterMachine(String id) {
        registeredMachines.remove(id);
    }

    @Nullable
    @Override
    public PluginModule createPluginModule() {
        return new KfinPluginModule();
    }

}
