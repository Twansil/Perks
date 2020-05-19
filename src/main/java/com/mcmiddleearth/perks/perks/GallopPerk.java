/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.perks.perks;

import com.mcmiddleearth.perks.commands.GallopHandler;
import com.mcmiddleearth.perks.permissions.Permissions;

/**
 *
 * @author Eriol_Eandur
 */
public final class GallopPerk extends Perk {
    
    public GallopPerk() {
        super("gallop");
        setCommandHandler(new GallopHandler(this, Permissions.USER.getPermissionNode()),"gallop","whoa");
    }
    
}
