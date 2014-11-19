/*
 * Copyright (C) 2004-2014 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.network;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.mmocore.network.IClientFactory;
import org.mmocore.network.IMMOExecutor;
import org.mmocore.network.IPacketHandler;
import org.mmocore.network.MMOConnection;
import org.mmocore.network.ReceivablePacket;

import com.l2jserver.Config;
import com.l2jserver.gameserver.network.L2GameClient.GameClientState;
import com.l2jserver.gameserver.network.clientpackets.*;
import com.l2jserver.util.Util;

/**
 * Stateful Packet Handler<BR>
 * The Stateful approach prevents the server from handling inconsistent packets, examples:<br>
 * <ul>
 * <li>Clients sends a MoveToLocation packet without having a character attached. (Potential errors handling the packet).</li>
 * <li>Clients sends a RequestAuthLogin being already authed. (Potential exploit).</li>
 * </ul>
 * Note: If for a given exception a packet needs to be handled on more then one state, then it should be added to all these states.
 * @author KenM
 */
public final class L2GamePacketHandler implements IPacketHandler<L2GameClient>, IClientFactory<L2GameClient>, IMMOExecutor<L2GameClient>
{
	private static final Logger _log = Logger.getLogger(L2GamePacketHandler.class.getName());
	
	// implementation
	@Override
	public ReceivablePacket<L2GameClient> handlePacket(ByteBuffer buf, L2GameClient client)
	{
		if (client.dropPacket())
		{
			return null;
		}
		
		int opcode = buf.get() & 0xFF;
		int id3;
		
		ReceivablePacket<L2GameClient> msg = null;
		GameClientState state = client.getState();
		// クライアントパケット解析用
		// Logger.getLogger(L2GameClientPacket.class.getName()).warning("state==>"+state.toString());
		// Logger.getLogger(L2GameClientPacket.class.getName()).warning("opcode==>"+opcode);
		switch (state)
		{
			case CONNECTED:
				switch (opcode)
				{
					case 0x0e:
						msg = new ProtocolVersion();
						break;
					case 0x2b:
						msg = new AuthLogin();
						break;
					default:
						printDebug(opcode, buf, state, client);
						break;
				}
				break;
			case AUTHED:
				switch (opcode)
				{
					case 0x00:
						msg = new Logout();
						break;
					case 0x0c:
						msg = new CharacterCreate();
						break;
					case 0x0d:
						msg = new CharacterDelete();
						break;
					case 0x12:
						msg = new CharacterSelect();
						break;
					case 0x13:
						msg = new NewCharacter();
						break;
					case 0x7b:
						msg = new CharacterRestore();
						break;
					case 0xd0:
						int id2 = -1;
						if (buf.remaining() >= 2)
						{
							id2 = buf.getShort() & 0xffff;
						}
						else
						{
							if (Config.PACKET_HANDLER_DEBUG)
							{
								_log.warning("Client: " + client.toString() + " sent a 0xd0 without the second opcode.");
							}
							break;
						}
						
						switch (id2)
						{
							case 0x33: // 603
								msg = new RequestGotoLobby();
								break;
							case 0xa6: // 603
								msg = new RequestEx2ndPasswordCheck();
								break;
							case 0xa7: // 603
								msg = new RequestEx2ndPasswordVerify();
								break;
							case 0xa8: // 603
								msg = new RequestEx2ndPasswordReq();
								break;
							case 0xa9: // 603
								msg = new RequestCharacterNameCreatable();
								break;
							default:
								printDebugDoubleOpcode(opcode, id2, buf, state, client);
						}
						break;
					default:
						printDebug(opcode, buf, state, client);
						break;
				}
				break;
			case IN_GAME:
				switch (opcode)
				{
					case 0x00:
						msg = new Logout();
						break;
					case 0x01:
						msg = new Attack();
						break;
					case 0x03:
						msg = new RequestStartPledgeWar();
						break;
					case 0x04:
						msg = new RequestReplyStartPledgeWar();
						break;
					case 0x05:
						msg = new RequestStopPledgeWar();
						break;
					case 0x06: // RequestSCCheck
						msg = new RequestReplyStopPledgeWar();
						break;
					case 0x07:
						msg = new RequestSurrenderPledgeWar();
						break;
					case 0x08:
						msg = new RequestReplySurrenderPledgeWar();
						break;
					case 0x09:
						msg = new RequestSetPledgeCrest();
						break;
					case 0x0b:
						msg = new RequestGiveNickName();
						break;
					case 0x0f:
						msg = new MoveBackwardToLocation();
						break;
					case 0x10:
						// Say
						break;
					case 0x11:
						msg = new EnterWorld();
						break;
					case 0x12:
						// CharacterSelect, in case of player spam clicks on loginscreen
						break;
					case 0x14:
						msg = new RequestItemList();
						break;
					case 0x15:
						// RequestEquipItem
						client.handleCheat("Used obsolete RequestEquipItem packet!");
						break;
					case 0x16:
						msg = new RequestUnEquipItem();
						break;
					case 0x17:
						msg = new RequestDropItem();
						break;
					case 0x19:
						msg = new UseItem();
						break;
					case 0x1a:
						msg = new TradeRequest();
						break;
					case 0x1b:
						msg = new AddTradeItem();
						break;
					case 0x1c:
						msg = new TradeDone();
						break;
					case 0x1f:
						msg = new Action();
						break;
					case 0x22:
						msg = new RequestLinkHtml();
						break;
					case 0x23:
						msg = new RequestBypassToServer();
						break;
					case 0x24:
						msg = new RequestBBSwrite();
						break;
					case 0x25:
						// RequestCreatePledge
						break;
					case 0x26:
						msg = new RequestJoinPledge();
						break;
					case 0x27:
						msg = new RequestAnswerJoinPledge();
						break;
					case 0x28:
						msg = new RequestWithdrawalPledge();
						break;
					case 0x29:
						msg = new RequestOustPledgeMember();
						break;
					case 0x2c:
						msg = new RequestGetItemFromPet();
						break;
					case 0x2e:
						msg = new RequestAllyInfo();
						break;
					case 0x2f:
						msg = new RequestCrystallizeItem();
						break;
					case 0x30:
						msg = new RequestPrivateStoreManageSell();
						break;
					case 0x31:
						msg = new SetPrivateStoreListSell();
						break;
					case 0x32:
						msg = new AttackRequest();
						break;
					case 0x33:
						// RequestTeleportPacket
						break;
					case 0x34:
						// msg = new RequestSocialAction();
						client.handleCheat("Used obsolete RequestSocialAction packet");
						break;
					case 0x35:
						// msg = new ChangeMoveType2();
						client.handleCheat("Used obsolete ChangeMoveType packet");
						break;
					case 0x36:
						// msg = new ChangeWaitType2();
						client.handleCheat("Used obsolete ChangeWaitType packet");
						break;
					case 0x37:
						msg = new RequestSellItem();
						break;
					case 0x38:
						// RequestMagicSkillList
						break;
					case 0x39:
						msg = new RequestMagicSkillUse();
						break;
					case 0x3a: // SendApperingPacket
						msg = new Appearing();
						break;
					case 0x3b:
						if (Config.ALLOW_WAREHOUSE)
						{
							msg = new SendWareHouseDepositList();
						}
						break;
					case 0x3c:
						msg = new SendWareHouseWithDrawList();
						break;
					case 0x3d:
						msg = new RequestShortCutReg();
						break;
					case 0x3f:
						msg = new RequestShortCutDel();
						break;
					case 0x40:
						msg = new RequestBuyItem();
						break;
					case 0x41:
						// RequestDismissPledge
						break;
					case 0x42:
						msg = new RequestJoinParty();
						break;
					case 0x43:
						msg = new RequestAnswerJoinParty();
						break;
					case 0x44:
						msg = new RequestWithDrawalParty();
						break;
					case 0x45:
						msg = new RequestOustPartyMember();
						break;
					case 0x46:
						// RequestDismissParty
						break;
					case 0x47:
						msg = new CannotMoveAnymore();
						break;
					case 0x48:
						msg = new RequestTargetCanceld();
						break;
					case 0x49:
						msg = new Say2();
						break;
					case 0x4a:
						int id_2 = -1;
						if (buf.remaining() >= 2)
						{
							id_2 = buf.getShort() & 0xffff;
						}
						else
						{
							if (Config.PACKET_HANDLER_DEBUG)
							{
								_log.warning("Client: " + client.toString() + " sent a 0x4a without the second opcode.");
							}
							break;
						}
						switch (id_2)
						{
							case 0x00:
								// SuperCmdCharacterInfo
								break;
							case 0x01:
								// SuperCmdSummonCmd
								break;
							case 0x02:
								// SuperCmdServerStatus
								break;
							case 0x03:
								// SendL2ParamSetting
								break;
							default:
								printDebugDoubleOpcode(opcode, id_2, buf, state, client);
								break;
						}
						break;
					case 0x4d:
						msg = new RequestPledgeMemberList();
						break;
					case 0x4f:
						// RequestMagicList
						break;
					case 0x50:
						msg = new RequestSkillList();
						break;
					case 0x52:
						msg = new MoveWithDelta();
						break;
					case 0x53:
						msg = new RequestGetOnVehicle();
						break;
					case 0x54:
						msg = new RequestGetOffVehicle();
						break;
					case 0x55:
						msg = new AnswerTradeRequest();
						break;
					case 0x56:
						msg = new RequestActionUse();
						break;
					case 0x57:
						msg = new RequestRestart();
						break;
					case 0x58:
						msg = new RequestSiegeInfo();
						break;
					case 0x59:
						msg = new ValidatePosition();
						break;
					case 0x5a:
						// RequestSEKCustom
						break;
					case 0x5b:
						msg = new StartRotating();
						break;
					case 0x5c:
						msg = new FinishRotating();
						break;
					case 0x5e:
						msg = new RequestShowBoard();
						break;
					case 0x5f:
						msg = new RequestEnchantItem();
						break;
					case 0x60:
						msg = new RequestDestroyItem();
						break;
					case 0x62:
						msg = new RequestQuestList();
						break;
					case 0x63: // RequestDestroyQuest
						msg = new RequestQuestAbort();
						break;
					case 0x65:
						msg = new RequestPledgeInfo();
						break;
					case 0x66:
						msg = new RequestPledgeExtendedInfo();
						break;
					case 0x67:
						msg = new RequestPledgeCrest();
						break;
					case 0x6a: // 603
						// New Request Friend List
						break;
					case 0x6b: // RequestSendL2FriendSay
						msg = new RequestSendFriendMsg();
						break;
					case 0x6c:
						msg = new RequestShowMiniMap();
						break;
					case 0x6d:
						// RequestSendMsnChatLog
						break;
					case 0x6e: // RequestReload
						msg = new RequestRecordInfo();
						break;
					case 0x6f:
						msg = new RequestHennaEquip();
						break;
					case 0x70:
						msg = new RequestHennaRemoveList();
						break;
					case 0x71:
						msg = new RequestHennaItemRemoveInfo();
						break;
					case 0x72:
						msg = new RequestHennaRemove();
						break;
					case 0x73:
						msg = new RequestAcquireSkillInfo();
						break;
					case 0x74:
						msg = new SendBypassBuildCmd();
						break;
					case 0x75:
						msg = new RequestMoveToLocationInVehicle();
						break;
					case 0x76:
						msg = new CannotMoveAnymoreInVehicle();
						break;
					case 0x77:
						msg = new RequestFriendInvite();
						break;
					case 0x78: // RequestFriendAddReply
						msg = new RequestAnswerFriendInvite();
						break;
					case 0x79: // 603 : Old Request Friend List
						msg = new RequestFriendList();
						break;
					case 0x7a:
						msg = new RequestFriendDel();
						break;
					case 0x7c:
						msg = new RequestAcquireSkill();
						break;
					case 0x7d:
						msg = new RequestRestartPoint();
						break;
					case 0x7e:
						msg = new RequestGMCommand();
						break;
					case 0x7f:
						msg = new RequestPartyMatchConfig();
						break;
					case 0x80:
						msg = new RequestPartyMatchList();
						break;
					case 0x81:
						msg = new RequestPartyMatchDetail();
						break;
					case 0x83: // SendPrivateStoreBuyList
						msg = new RequestPrivateStoreBuy();
						break;
					case 0x85:
						msg = new RequestTutorialLinkHtml();
						break;
					case 0x86:
						msg = new RequestTutorialPassCmdToServer();
						break;
					case 0x87:
						msg = new RequestTutorialQuestionMark();
						break;
					case 0x88:
						msg = new RequestTutorialClientEvent();
						break;
					case 0x89:
						msg = new RequestPetition();
						break;
					case 0x8a:
						msg = new RequestPetitionCancel();
						break;
					case 0x8b:
						msg = new RequestGmList();
						break;
					case 0x8c:
						msg = new RequestJoinAlly();
						break;
					case 0x8d:
						msg = new RequestAnswerJoinAlly();
						break;
					case 0x8e: // RequestWithdrawAlly
						msg = new AllyLeave();
						break;
					case 0x8f: // RequestOustAlly
						msg = new AllyDismiss();
						break;
					case 0x90:
						msg = new RequestDismissAlly();
						break;
					case 0x91:
						msg = new RequestSetAllyCrest();
						break;
					case 0x92:
						msg = new RequestAllyCrest();
						break;
					case 0x93:
						msg = new RequestChangePetName();
						break;
					case 0x94:
						msg = new RequestPetUseItem();
						break;
					case 0x95:
						msg = new RequestGiveItemToPet();
						break;
					case 0x96:
						msg = new RequestPrivateStoreQuitSell();
						break;
					case 0x97:
						msg = new SetPrivateStoreMsgSell();
						break;
					case 0x98:
						msg = new RequestPetGetItem();
						break;
					case 0x99:
						msg = new RequestPrivateStoreManageBuy();
						break;
					case 0x9a: // SetPrivateStoreList
						msg = new SetPrivateStoreListBuy();
						break;
					case 0x9b: // 603
						break;
					case 0x9c:
						msg = new RequestPrivateStoreQuitBuy();
						break;
					case 0x9d:
						msg = new SetPrivateStoreMsgBuy();
						break;
					case 0x9f: // SendPrivateStoreBuyList
						msg = new RequestPrivateStoreSell();
						break;
					case 0xa0:
						// SendTimeCheckPacket
						break;
					case 0xa6:
						// RequestSkillCoolTime
						break;
					case 0xa7:
						msg = new RequestPackageSendableItemList();
						break;
					case 0xa8:
						msg = new RequestPackageSend();
						break;
					case 0xa9:
						msg = new RequestBlock();
						break;
					case 0xaa:
						msg = new RequestSiegeInfo();
						break;
					case 0xab: // RequestCastleSiegeAttackerList
						msg = new RequestSiegeAttackerList();
						break;
					case 0xac:
						msg = new RequestSiegeDefenderList();
						break;
					case 0xad: // RequestJoinCastleSiege
						msg = new RequestJoinSiege();
						break;
					case 0xae: // RequestConfirmCastleSiegeWaitingList
						msg = new RequestConfirmSiegeWaitingList();
						break;
					case 0xAF:
						msg = new RequestSetCastleSiegeTime();
						break;
					case 0xb0:
						msg = new MultiSellChoose();
						break;
					case 0xb1:
						// NetPing
						break;
					case 0xb2:
						// RequestRemainTime
						break;
					case 0xb3:
						msg = new BypassUserCmd();
						break;
					case 0xb4:
						msg = new SnoopQuit();
						break;
					case 0xb5:
						msg = new RequestRecipeBookOpen();
						break;
					case 0xb6: // RequestRecipeItemDelete
						msg = new RequestRecipeBookDestroy();
						break;
					case 0xb7:
						msg = new RequestRecipeItemMakeInfo();
						break;
					case 0xb8:
						msg = new RequestRecipeItemMakeSelf();
						break;
					case 0xb9:
						// RequestRecipeShopManageList
						break;
					case 0xba:
						msg = new RequestRecipeShopMessageSet();
						break;
					case 0xbb:
						msg = new RequestRecipeShopListSet();
						break;
					case 0xbc:
						msg = new RequestRecipeShopManageQuit();
						break;
					case 0xbd:
						// RequestRecipeShopManageCancel
						break;
					case 0xbe:
						msg = new RequestRecipeShopMakeInfo();
						break;
					case 0xbf: // RequestRecipeShopMakeDo
						msg = new RequestRecipeShopMakeItem();
						break;
					case 0xc0: // RequestRecipeShopSellList
						msg = new RequestRecipeShopManagePrev();
						break;
					case 0xc1: // RequestObserverEndPacket
						msg = new ObserverReturn();
						break;
					case 0xc2:
						// Unused (RequestEvaluate/VoteSociality)
						break;
					case 0xc3:
						msg = new RequestHennaItemList();
						break;
					case 0xc4:
						msg = new RequestHennaItemInfo();
						break;
					case 0xc5:
						msg = new RequestBuySeed();
						break;
					case 0xc6: // ConfirmDlg
						msg = new DlgAnswer();
						break;
					case 0xc7: // RequestPreviewItem
						msg = new RequestPreviewItem();
						break;
					case 0xc8:
						msg = new RequestSSQStatus();
						break;
					case 0xc9:
						msg = new RequestPetitionFeedback();
						break;
					case 0xcb:
						msg = new GameGuardReply();
						break;
					case 0xcc:
						msg = new RequestPledgePower();
						break;
					case 0xcd:
						msg = new RequestMakeMacro();
						break;
					case 0xce:
						msg = new RequestDeleteMacro();
						break;
					case 0xcf: // RequestProcureCrop
						// msg = new RequestBuyProcure();
						break;
					
					case 0xd0:
						int id2 = -1;
						if (buf.remaining() >= 2)
						{
							id2 = buf.getShort() & 0xffff;
						}
						else
						{
							if (Config.PACKET_HANDLER_DEBUG)
							{
								_log.warning("Client: " + client.toString() + " sent a 0xd0 without the second opcode.");
							}
							break;
						}
						
						switch (id2)
						{
							case 0x01:
								msg = new RequestManorList();
								break;
							case 0x02:
								msg = new RequestProcureCropList();
								break;
							case 0x03:
								msg = new RequestSetSeed();
								break;
							case 0x04:
								msg = new RequestSetCrop();
								break;
							case 0x05:
								msg = new RequestWriteHeroWords();
								break;
							case 0x5C: // 603
								/**
								 * Server Packets: ExMpccRoomInfo FE:9B ExListMpccWaiting FE:9C ExDissmissMpccRoom FE:9D ExManageMpccRoomMember FE:9E ExMpccRoomMember FE:9F
								 */
								// TODO: RequestJoinMpccRoom chdd
								break;
							case 0x5B: // 603
								// TODO: RequestManageMpccRoom chdddddS
								break;
							case 0x5A: // 603
								// TODO: RequestListMpccWaiting chddd
								break;
							case 0x5D: // 603
								// RequestOustFromMpccRoom
								break;
							case 0x5E: // 603
								// RequestDismissMpccRoom
								break;
							case 0x5F: // 603
								// RequestWithdrawMpccRoom
								break;
							case 0x61: // 603
								// RequestMpccPartymasterList
								break;
							case 0x06:
								msg = new RequestExAskJoinMPCC();
								break;
							case 0x07:
								msg = new RequestExAcceptJoinMPCC();
								break;
							case 0x08:
								msg = new RequestExOustFromMPCC();
								break;
							case 0x09:
								msg = new RequestOustFromPartyRoom();
								break;
							case 0x0a:
								msg = new RequestDismissPartyRoom();
								break;
							case 0x0b:
								msg = new RequestWithdrawPartyRoom();
								break;
							case 0x0c:
								msg = new RequestChangePartyLeader();
								break;
							case 0x0d:
								msg = new RequestAutoSoulShot();
								break;
							case 0x0e:
								msg = new RequestExEnchantSkillInfo();
								break;
							case 0x0f:
								msg = new RequestExEnchantSkill();
								break;
							case 0x10:
								msg = new RequestExPledgeCrestLarge();
								break;
							case 0x11:
								msg = new RequestExSetPledgeCrestLarge();
								break;
							case 0x12:
								msg = new RequestPledgeSetAcademyMaster();
								break;
							case 0x13:
								msg = new RequestPledgePowerGradeList();
								break;
							case 0x14:
								msg = new RequestPledgeMemberPowerInfo();
								break;
							case 0x15:
								msg = new RequestPledgeSetMemberPowerGrade();
								break;
							case 0x16:
								msg = new RequestPledgeMemberInfo();
								break;
							case 0x17:
								msg = new RequestPledgeWarList();
								break;
							case 0x18:
								msg = new RequestExFishRanking();
								break;
							case 0x19:
								msg = new RequestPCCafeCouponUse();
								break;
							case 0x1b:
								msg = new RequestDuelStart();
								break;
							case 0x1c:
								msg = new RequestDuelAnswerStart();
								break;
							case 0x1d:
								// RequestExSetTutorial
								break;
							case 0x1e:
								msg = new RequestExRqItemLink();
								break;
							case 0x1f:
								// CanNotMoveAnymoreAirShip
								break;
							case 0x20:
								msg = new MoveToLocationInAirShip();
								break;
							case 0x21:
								msg = new RequestKeyMapping();
								break;
							case 0x22:
								msg = new RequestSaveKeyMapping();
								break;
							case 0x23:
								msg = new RequestExRemoveItemAttribute();
								break;
							case 0x24:
								msg = new RequestSaveInventoryOrder();
								break;
							case 0x25:
								msg = new RequestExitPartyMatchingWaitingRoom();
								break;
							case 0x26:
								msg = new RequestConfirmTargetItem();
								break;
							case 0x27:
								msg = new RequestConfirmRefinerItem();
								break;
							case 0x28:
								msg = new RequestConfirmGemStone();
								break;
							case 0x29:
								msg = new RequestOlympiadObserverEnd();
								break;
							case 0x2a:
								msg = new RequestCursedWeaponList();
								break;
							case 0x2b:
								msg = new RequestCursedWeaponLocation();
								break;
							case 0x2c:
								msg = new RequestPledgeReorganizeMember();
								break;
							case 0x2d:
								msg = new RequestExMPCCShowPartyMembersInfo();
								break;
							case 0x2e:
								msg = new RequestOlympiadMatchList();
								break;
							case 0x2f:
								msg = new RequestAskJoinPartyRoom();
								break;
							case 0x30:
								msg = new AnswerJoinPartyRoom();
								break;
							case 0x31:
								msg = new RequestListPartyMatchingWaitingRoom();
								break;
							/* not used and delete
							case 0x32:
								msg = new RequestExEnchantSkillSafe();
								break;
							case 0x33:
								msg = new RequestExEnchantSkillUntrain();
								break;
							case 0x34:
								msg = new RequestExEnchantSkillRouteChange();
								break;
							 */
							case 0x32: // 603
								msg = new RequestExEnchantItemAttribute();
								break;
							/* not exist
							case 0x36:
								msg = new ExGetOnAirShip();
								break;
							 */
							case 0x35: // 603
								msg = new MoveToLocationAirShip();
								break;
							case 0x36: // 603
								msg = new RequestBidItemAuction();
								break;
							case 0x37: // 603
								msg = new RequestInfoItemAuction();
								break;
							case 0x38: // 603
								msg = new RequestExChangeName();
								break;
							case 0x39: // 603
								msg = new RequestAllCastleInfo();
								break;
							case 0x3a: // 603
								msg = new RequestAllFortressInfo();
								break;
							case 0x3b: // 603
								msg = new RequestAllAgitInfo();
								break;
							case 0x3c: // 603
								msg = new RequestFortressSiegeInfo();
								break;
							case 0x3d: // 603
								msg = new RequestGetBossRecord();
								break;
							case 0x3e: // 603
								msg = new RequestRefine();
								break;
							case 0x3f: // 603
								msg = new RequestConfirmCancelItem();
								break;
							case 0x40: // 603
								msg = new RequestRefineCancel();
								break;
							case 0x41: // 603
								msg = new RequestExMagicSkillUseGround();
								break;
							case 0x42: // 603
								msg = new RequestDuelSurrender();
								break;
							case 0x43: // 603
								msg = new RequestExEnchantSkillInfoDetail();
								break;
							case 0x45: // 603
								msg = new RequestFortressMapInfo();
								break;
							case 0x46: // 603
								// RequestPVPMatchRecord
								break;
							case 0x47: // 603
								msg = new SetPrivateStoreWholeMsg();
								break;
							case 0x48: // 603
								msg = new RequestDispel();
								break;
							case 0x49: // 603
								msg = new RequestExTryToPutEnchantTargetItem();
								break;
							case 0x4a: // 603
								msg = new RequestExTryToPutEnchantSupportItem();
								break;
							case 0x4b: // 603
								msg = new RequestExCancelEnchantItem();
								break;
							case 0x4c: // 603
								msg = new RequestChangeNicknameColor();
								break;
							case 0x4d: // 603
								msg = new RequestResetNickname();
								break;
							case 0x4e: // 603
								id3 = 0;
								if (buf.remaining() >= 4)
								{
									id3 = buf.getInt();
								}
								else
								{
									_log.warning("Client: " + client.toString() + " sent a 0xd0:0x51 without the third opcode.");
									break;
								}
								switch (id3)
								{
									case 0x00:
										msg = new RequestBookMarkSlotInfo();
										break;
									case 0x01:
										msg = new RequestSaveBookMarkSlot();
										break;
									case 0x02:
										msg = new RequestModifyBookMarkSlot();
										break;
									case 0x03:
										msg = new RequestDeleteBookMarkSlot();
										break;
									case 0x04:
										msg = new RequestTeleportBookMark();
										break;
									case 0x05:
										// RequestChangeBookMarkSlot
										break;
									default:
										printDebugDoubleOpcode(opcode, id3, buf, state, client);
										break;
								}
								break;
							case 0x4f: // 603
								msg = new RequestWithDrawPremiumItem();
								break;
							case 0x50: // 603
								// RequestJump
								break;
							case 0x51: // 603
								// RequestStartShowCrataeCubeRank
								break;
							case 0x52: // 603
								// RequestStopShowCrataeCubeRank
								break;
							case 0x53: // 603
								// NotifyStartMiniGame
								break;
							case 0x54: // 603
								msg = new RequestJoinDominionWar();
								break;
							case 0x55: // 603
								msg = new RequestDominionInfo();
								break;
							case 0x56: // 603
								// RequestExCleftEnter
								break;
							case 0x57: // 603
								msg = new RequestExCubeGameChangeTeam();
								break;
							case 0x58: // 603
								msg = new EndScenePlayer();
								break;
							case 0x59: // 603
								msg = new RequestExCubeGameReadyAnswer();
								break;
							case 0x60: // 603
								msg = new RequestSeedPhase();
								break;
							case 0x62: // 603
								msg = new RequestPostItemList();
								break;
							case 0x63: // 603
								msg = new RequestSendPost();
								break;
							case 0x64: // 603
								msg = new RequestReceivedPostList();
								break;
							case 0x65: // 603
								msg = new RequestDeleteReceivedPost();
								break;
							case 0x66: // 603
								msg = new RequestReceivedPost();
								break;
							case 0x67: // 603
								msg = new RequestPostAttachment();
								break;
							case 0x68: // 603
								msg = new RequestRejectPostAttachment();
								break;
							case 0x69: // 603
								msg = new RequestSentPostList();
								break;
							case 0x6a: // 603
								msg = new RequestDeleteSentPost();
								break;
							case 0x6b: // 603
								msg = new RequestSentPost();
								break;
							case 0x6c: // 603
								msg = new RequestCancelPostAttachment();
								break;
							case 0x6d: // 603
								// RequestShowNewUserPetition
								break;
							case 0x6e: // 603
								// RequestShowStepThree
								break;
							case 0x6f: // 603
								// RequestShowStepTwo
								break;
							case 0x70: // 603
								// ExRaidReserveResult
								break;
							case 0x71: // 603
								// ExCloseRaidSocket
								break;
							case 0x72: // 603
								msg = new RequestRefundItem();
								break;
							case 0x73: // 603
								msg = new RequestBuySellUIClose();
								break;
							case 0x74: // 603
								// RequestEventMatchObserverEnd
								break;
							case 0x75: // 603
								msg = new RequestPartyLootModification();
								break;
							case 0x76: // 603
								msg = new AnswerPartyLootModification();
								break;
							case 0x77: // 603
								msg = new AnswerCoupleAction();
								break;
							case 0x78: // 603
								msg = new BrEventRankerList();
								break;
							case 0x79: // 603
								// AskMembership
								break;
							case 0x7a: // 603
								// RequestAddExpandQuestAlarm
								break;
							case 0x7b: // 603
								msg = new RequestVoteNew();
								break;
							case 0x81: // 603
								msg = new RequestExAddContactToContactList();
								break;
							case 0x82: // 603
								msg = new RequestExDeleteContactFromContactList();
								break;
							case 0x83: // 603
								msg = new RequestExShowContactList();
								break;
							case 0x84: // 603
								msg = new RequestExFriendListExtended();
								break;
							case 0x85: // 603
								msg = new RequestExOlympiadMatchListRefresh();
								break;
							case 0x86: // 603
								// RequestBRGamePoint
								break;
							case 0x87: // 603
								// RequestBRProductList
								break;
							case 0x88: // 603
								// RequestBRProductInfo
								break;
							case 0x89: // 603
								// RequestBRBuyProduct
								break;
							case 0x8a: // 603
								// RequestBRRecentProductList
								break;
							case 0x8b: // 603
								// BrMinigameLoadScores
								break;
							case 0x8c: // 603
								// BrMinigameInsertScore
								break;
							case 0x8d: // 603
								// BrLectureMark
								break;
							case 0xae: // 603
								// RequestGoodsInventoryInfo
								break;
							case 0xaf: // 603
								// RequestUseGoodsInventoryItem
								break;
							// 603-Start
							case 0x8e: // 603
								msg = new RequestCrystallizeEstimate();
								break;
							case 0x91: // 603
								msg = new RequestFlyMove();
								break;
							case 0xa1: // 603
								msg = new RequestCallToChangeClass();
								break;
							case 0xa2: // 603
								msg = new RequestChangeToAwakenedClass();
								break;
							case 0xad: // 603
								msg = new RequestFlyMoveStart();
								break;
							case 0xba: // 603
								msg = new RequestInzoneWaitingTime();
								break;
							case 0xe3: // 603
								msg = new RequestExAddEnchantScrollItem();
								break;
							case 0xe4: // 603
								msg = new RequestExRemoveEnchantSupportItem();
								break;
							case 0x7c: // 603
							case 0x7d: // 603
							case 0x7e: // 603
							case 0x7f: // 603
							case 0x80: // 603
							case 0x8f: // 603
							case 0x90: // 603
							case 0x92: // 603
							case 0x93: // 603
							case 0x94: // 603
							case 0x95: // 603
							case 0x96: // 603
							case 0x97: // 603
							case 0x98: // 603
							case 0x99: // 603
							case 0x9a: // 603
							case 0x9b: // 603
							case 0x9c: // 603
							case 0x9d: // 603
							case 0x9e: // 603
							case 0x9f: // 603
							case 0xa0: // 603
							case 0xa3: // 603
							case 0xa4: // 603
							case 0xa5: // 603
							case 0xa6: // 603
							case 0xa7: // 603
							case 0xa8: // 603
							case 0xa9: // 603
							case 0xb0: // 603
							case 0xb1: // 603
							case 0xb2: // 603
							case 0xb4: // 603
							case 0xb5: // 603
							case 0xb6: // 603
							case 0xb7: // 603
							case 0xb8: // 603
							case 0xb9: // 603
							case 0xbb: // 603
							case 0xbc: // 603
							case 0xbd: // 603
							case 0xbe: // 603
							case 0xbf: // 603
							case 0xc0: // 603
							case 0xc1: // 603
							case 0xc2: // 603
							case 0xc3: // 603
							case 0xc4: // 603
							case 0xc5: // 603
							case 0xc6: // 603
							case 0xc7: // 603
							case 0xc9: // 603
							case 0xca: // 603
							case 0xcb: // 603
							case 0xcc: // 603
							case 0xcd: // 603
							case 0xce: // 603
							case 0xcf: // 603
							case 0xd0: // 603
							case 0xd2: // 603
							case 0xd4: // 603
							case 0xd5: // 603
							case 0xd6: // 603
							case 0xd7: // 603
							case 0xd8: // 603
							case 0xd9: // 603
							case 0xda: // 603
							case 0xdb: // 603
							case 0xdc: // 603
							case 0xdd: // 603
							case 0xde: // 603
							case 0xdf: // 603
							case 0xe0: // 603
							case 0xe1: // 603
							case 0xe2: // 603
							case 0xe5: // 603
							case 0xe6: // 603
							case 0xe7: // 603
							case 0xe8: // 603
							case 0xe9: // 603
							case 0xea: // 603
							case 0xeb: // 603
							case 0xec: // 603
							case 0xed: // 603
							case 0xee: // 603
							case 0xef: // 603
							case 0xf0: // 603
							case 0xf1: // 603
							case 0xf2: // 603
							case 0xf3: // 603
							case 0xf4: // 603
							case 0xf6: // 603
							case 0xf7: // 603
							case 0xf8: // 603
							case 0xf9: // 603
							case 0xfa: // 603
							case 0xfb: // 603
							case 0xfc: // 603
							case 0xfd: // 603
							case 0xfe: // 603
							case 0xff: // 603
							case 0x100: // 603
							case 0x101: // 603
							case 0x102: // 603
								break;
							// 603-End
							default:
								printDebugDoubleOpcode(opcode, id2, buf, state, client);
								break;
						}
						break;
					default:
						printDebug(opcode, buf, state, client);
						break;
				}
				break;
		}
		return msg;
	}
	
	private void printDebug(int opcode, ByteBuffer buf, GameClientState state, L2GameClient client)
	{
		client.onUnknownPacket();
		if (!Config.PACKET_HANDLER_DEBUG)
		{
			return;
		}
		
		int size = buf.remaining();
		_log.warning("Unknown Packet: 0x" + Integer.toHexString(opcode) + " on State: " + state.name() + " Client: " + client.toString());
		byte[] array = new byte[size];
		buf.get(array);
		_log.warning(Util.printData(array, size));
	}
	
	private void printDebugDoubleOpcode(int opcode, int id2, ByteBuffer buf, GameClientState state, L2GameClient client)
	{
		client.onUnknownPacket();
		if (!Config.PACKET_HANDLER_DEBUG)
		{
			return;
		}
		
		int size = buf.remaining();
		_log.warning("Unknown Packet: 0x" + Integer.toHexString(opcode) + ":0x" + Integer.toHexString(id2) + " on State: " + state.name() + " Client: " + client.toString());
		byte[] array = new byte[size];
		buf.get(array);
		_log.warning(Util.printData(array, size));
	}
	
	// impl
	@Override
	public L2GameClient create(MMOConnection<L2GameClient> con)
	{
		return new L2GameClient(con);
	}
	
	@Override
	public void execute(ReceivablePacket<L2GameClient> rp)
	{
		rp.getClient().execute(rp);
	}
}
