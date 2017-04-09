/*
 * MIT License
 *
 * Copyright (c) 2017 Connor (Vectrix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.connorhartley.elderguardian;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = "elderguardian",
        name = "ElderGuardian",
        version = "1.11.2-6.0.0-01",
        description = "An optional Mixin Mod for Guardian that adds extra utilities otherwise inaccessible from the API.",
        authors = {
                "Connor Hartley (vectrix)"
        }
)
public class ElderGuardian {

        @Inject
        private Logger logger;

        @Inject
        private PluginContainer container;

        @Listener
        public void onGamePreInitialization(GamePreInitializationEvent event) {
                if (PluginState.getState().equals(State.LOADING)) {
                        PluginState.setState(State.LOADED);

                        this.logger.info("Loaded ElderGuardian " + container.getVersion().get() + " mixin optimizations.");
                } else if (PluginState.getState().equals(State.LOADED)) {
                        this.logger.error("Incorrect state. Mixins are already loaded!");
                } else if (PluginState.getState().equals(State.UNLOADED)) {
                        this.logger.warn("Did not load mixins.");
                }
        }

        @Listener
        public void onGameInitialization(GameInitializationEvent event) {
                if (!PluginState.getState().equals(State.LOADED)) {
                        this.logger.error("ElderGuardian has not been able to load its mixins. Is this in the '/mods' folder?");
                }
        }

        public enum State {
                UNLOADED,
                LOADING,
                LOADED
        }
}
