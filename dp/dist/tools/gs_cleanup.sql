-- These queries are meant to delete any gameserver table you may have from previous
-- L2J installations.
--
-- Queries that create these tables and/or populate them apropiately are available in
-- the ../sql/ folder.
--
-- See the documentation.txt file for more details.  

DROP TABLE IF EXISTS 
account_gsdata,
airships,
auction,
auction_bid,
auction_watch,
auto_announcements,
buylists,
castle,
castle_doorupgrade,
castle_functions,
castle_manor_procure,
castle_manor_production,
castle_siege_guards,
character_contacts,
character_friends,
character_hennas,
character_instance_time,
character_item_reuse_save,
character_macroses,
character_norestart_zone_time,
character_offline_trade_items,
character_offline_trade,
character_pet_skills_save,
character_quest_global_data,
character_quests,
character_raid_points,
character_recipebook,
character_recipeshoplist,
character_reco_bonus,
character_shortcuts,
character_skills,
character_skills_save,
character_subclasses,
character_summons,
character_summon_skills_save,
character_tpbookmark,
character_ui_actions,
character_ui_categories,
character_variables,
characters,
clan_data,
clan_notices,
clan_privs,
clan_skills,
clan_subpledges,
clan_wars,
clanhall,
clanhall_functions,
clanhall_siege_attackers,
clanhall_siege_guards,
crest,
cursed_weapons,
dimensional_rift,
droplist,
fort,
fort_doorupgrade,
fort_functions,
fort_siege_guards,
fort_spawnlist,
fortsiege_clans,
forums,
four_sepulchers_spawnlist,
games,
global_tasks,
global_variables,
grandboss_data,
grandboss_list,
hellbound_spawnlist,
herb_droplist_groups,
heroes,
heroes_diary,
item_attributes,
item_auction_bid,
item_auction,
item_elementals,
items,
itemsonground,
locations,
merchant_lease,
messages,
minions,
mods_wedding,
npc,
npc_buffer,
npcaidata,
npc_elementals,
npcskills,
olympiad_data,
olympiad_fights,
olympiad_nobles,
olympiad_nobles_eom,
petition_feedback,
pets,
pets_skills,
posts,
punishments,
quest_global_data,
raidboss_spawnlist,
rainbowsprings_attacker_list,
random_spawn,
random_spawn_loc,
seven_signs,
seven_signs_festival,
seven_signs_status,
siegable_clanhall,
siegable_hall_flagwar_attackers,
siegable_hall_flagwar_attackers_members,
siege_clans,
spawnlist,
teleport,
territories,
territory_registrations,
territory_spawnlist,
topic,
chatdata,
npcCharData,
messagetable,
zzz_skill_name,
zzz_item_name,
zzz_npc_name;