MCME-Tours
==========

http://ci.mcme.co/job/MCMETours/

Tour management plugin.<br/>
<i>Allows a 'guide' to create a temporary group that other players can join.<br/>
Guides then have access to teleportation commands that can only be used on members of that group.<br/></i>
<br/>
Features:
- Dynamic creation/removal of Ranger-made 'tour groups' (one tour per Ranger at any given time).
- Run multiple tours simultaneously.
- Tour group chat highlights/prefixes for tour members only.
- Automatic server announcements of tours, and player tour-join/leave notifications.
- 'Safe' teleport commands that only affect players who have 'joined' the tour group.
- Give users useful information on currently running tours, else a list of online Rangers (if any).
- Alert Rangers when a new-to-the-server player joins.<br/>

<br/>
All users with '<b>MCMETours.user</b>' permission node can:
- '<b>/tour</b>' to find info on tours.
- '<b>/tour help</b>' to find info on MCMETours commands.
- '<b>/tour join \<tourname\></b>' to join a currently running tour.
- '<b>/tour leave</b>' to leave the currently running tour.<br/>
- '<b>/tour request</b>' sends an alert to any Rangers online that use is looking for a tour.<br/>

<br/>
Users with the '<b>MCMETours.ranger</b>' permission node can:
- '<b>/tour start</b>' creates new tour, names it after user, and announces it.
- '<b>/tour stop</b>' stops user's current tour and removes all group members.
- '<b>/tour list</b>' lists all users in the tour group.
- '<b>/tourtp \<player\></b>' teleports \<player\> to user. \<player\> must be part of tour group.
- '<b>/ttp \<player\></b>' shortcut for tourtp.
- '<b>/tourtpa</b>' teleports all players in tour group to user.
- '<b>/ttpa</b>' shortcut for tourtpa.

<br/>
Users with the '<b>MCMETours.admin</b>' permission node can:
- '<b>/tours</b>' lists available admin command arguments.
- '<b>/tours reset</b>' cancels all tours and resets all groups/lists.
- '<b>/tours broadcast</b>' toggles public tour announcements on and off.
- '<b>tours info</b>' lists all config settings and their values.
- '<b>/tours debug</b>' prints all group and list entries.<br/>
<br/>
