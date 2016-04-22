/*This file is part of RestrictedEnchanting, licensed under the MIT License (MIT).
*
* Copyright (c) 2016 Hunter Sharpe
* Copyright (c) contributors

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package net.huntersharpe.RestrictedEnchanting;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;

@Plugin(id="restrictedenchanting", name="RestrictedEnchanting", version="1.0")
public class RestrictedEnchanting {

    private RestrictionHandler handler;

    public double version = 1.0;

    @Inject
    Game game;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public File configFile = null;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> loader = null;

    public CommentedConfigurationNode config = null;

    @Listener
    public void onPreInit(GamePreInitializationEvent e){
        try{
            if(!configFile.exists()){
                configFile.createNewFile();
                config = loader.load();
                loader.createEmptyNode();
                loader.save(config);
            }
            config = loader.load();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        game.getCommandManager().register(this, restrictions, "re", "restrictions");
        game.getEventManager().registerListeners(this, new EnchantmentListener(this));
        handler = new RestrictionHandler(this);
    }

    CommandSpec set = CommandSpec.builder()
            .arguments(GenericArguments.seq(
                    GenericArguments.string(Text.of("item")),
                    GenericArguments.string(Text.of("enchant")),
                    GenericArguments.integer(Text.of("value"))
            ))
            .executor(new ChildSet(this))
            .build();
    CommandSpec remove = CommandSpec.builder()
            .arguments(GenericArguments.seq(
                    GenericArguments.string(Text.of("item")),
                    GenericArguments.string(Text.of("enchant")))
            )
            .executor(new ChildRemove(this))
            .build();
    CommandSpec restrictions = CommandSpec.builder()
            .child(set, "remove", "rem")
            .child(remove, "set", "add")
            .executor(new RestrictionParent(this))
            .build();

    public CommentedConfigurationNode getConfig(){
        return this.config;
    }

}