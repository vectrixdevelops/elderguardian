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

import io.github.connorhartley.elderguardian.storage.StorageConsumer;
import io.github.connorhartley.elderguardian.storage.StorageKey;
import io.github.connorhartley.elderguardian.storage.StorageProvider;
import io.github.connorhartley.elderguardian.storage.StorageValue;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ElderGuardianConfiguration implements StorageProvider<File> {

    public static ElderGuardianConfiguration INSTANCE = new ElderGuardianConfiguration();

    private CommentedConfigurationNode configurationNode;

    private final File configFile;
    private final ConfigurationLoader<CommentedConfigurationNode> configManager;

    public StorageValue<String, Boolean> configModifyExploitMixin;

    private ElderGuardianConfiguration() {
        this.configFile = Paths.get("config", "guardian", "elderguardian.conf").toFile();
        this.configManager = HoconConfigurationLoader.builder().setFile(this.configFile).build();

        this.create();
        this.load();
        this.update();
    }

    @Override
    public void create() {
        try {
            if (!this.exists()) {
                this.configFile.getParentFile().mkdirs();
                this.configFile.createNewFile();
            }

            this.configurationNode = this.configManager.load();

            this.configModifyExploitMixin = new StorageValue<>(new StorageKey<>("modify-exploit-mixin"),
                    "Modifies or disables built in exploits that conflict with Guardian.", true, null);

            this.configModifyExploitMixin.<ConfigurationNode>createStorage(this.configurationNode);

            this.configManager.save(this.configurationNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            if (this.exists()) {
                this.configurationNode = this.configManager.load();

                this.configModifyExploitMixin.<ConfigurationNode>loadStorage(this.configurationNode);

                this.configManager.save(this.configurationNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        try {
            if (this.exists()) {
                this.configurationNode = this.configManager.load();

                this.configModifyExploitMixin.<ConfigurationNode>updateStorage(this.configurationNode);

                this.configManager.save(this.configurationNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists() {
        return this.configFile.exists();
    }

    @Override
    public File getLocation() {
        return this.configFile;
    }
}
