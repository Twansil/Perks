/*
 * Copyright (c) 2015 Dallen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * 
 */
package com.mcmiddleearth.perks.utils;

import org.bukkit.util.Vector;

/**
 *
 * @author Dallen
 */

//Thanks for the help SethBling :P

public class VelocityUtil
{
    public static Vector calculateVelocity(Vector from, Vector to, int heightGain)
    {
        double gravity = 0.039;
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));
        int gain = heightGain;
        double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;
        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
        double vy = Math.sqrt(maxGain * gravity);
        double vh = vy / slope;
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;
        double vx = vh * dirx;
        double vz = vh * dirz;
        Vector rtn = new Vector(vx, vy, vz);
        return rtn;
    }
 
    private static double distanceSquared(Vector from, Vector to)
    {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();
 
        return dx * dx + dz * dz;
    }
}
