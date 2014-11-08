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
package com.l2jserver.gameserver.datatables;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.gameserver.engines.DocumentParser;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.base.ClassInfo;

/**
 * This class holds the list of classes and it's info.<br>
 * It's in <i>beta</i> state, so it's expected to change over time.
 * @author Zoey76
 */
public final class ClassListData extends DocumentParser
{
	private static final Map<ClassId, ClassInfo> _classData = new HashMap<>();
	
	/**
	 * Instantiates a new class list data.
	 */
	protected ClassListData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_classData.clear();
		parseDatapackFile("data/stats/chars/classList.xml");
		_log.info(getClass().getSimpleName() + ": Loaded " + _classData.size() + " Class data.");
	}
	
	@Override
	protected void parseDocument()
	{
		NamedNodeMap attrs;
		Node attr;
		ClassId classId;
		String className;
		String classServName;
		ClassId parentClassId;
		for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equals(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					attrs = d.getAttributes();
					if ("class".equals(d.getNodeName()))
					{
						attr = attrs.getNamedItem("classId");
						classId = ClassId.getClassId(parseInteger(attr));
						attr = attrs.getNamedItem("name");
						className = attr.getNodeValue();
						attr = attrs.getNamedItem("serverName");
						classServName = attr.getNodeValue();
						attr = attrs.getNamedItem("parentClassId");
						parentClassId = (attr != null) ? ClassId.getClassId(parseInteger(attr)) : null;
						_classData.put(classId, new ClassInfo(classId, className, classServName, parentClassId));
					}
				}
			}
		}
	}
	
	/**
	 * Gets the class list.
	 * @return the complete class list.
	 */
	public Map<ClassId, ClassInfo> getClassList()
	{
		return _classData;
	}
	
	/**
	 * Gets the class info.
	 * @param classId the class Id.
	 * @return the class info related to the given {@code classId}.
	 */
	public ClassInfo getClass(final ClassId classId)
	{
		return _classData.get(classId);
	}
	
	/**
	 * Gets the class info.
	 * @param classId the class Id as integer.
	 * @return the class info related to the given {@code classId}.
	 */
	public ClassInfo getClass(final int classId)
	{
		final ClassId id = ClassId.getClassId(classId);
		return (id != null) ? _classData.get(id) : null;
	}
	
	/**
	 * Gets the class info.
	 * @param classServName the server side class name.
	 * @return the class info related to the given {@code classServName}.
	 */
	public ClassInfo getClass(final String classServName)
	{
		for (final ClassInfo classInfo : _classData.values())
		{
			if (classInfo.getClassServName().equals(classServName))
			{
				return classInfo;
			}
		}
		return null;
	}
	
	public final String getJPClassNameById(int classId)
	{
		HashMap<Integer,String> classMap = new HashMap<Integer,String>();
		classMap.put(0, "ヒューマン ファイター");
		classMap.put(1, "ウォーリアー");
		classMap.put(2, "グラディエーター");
		classMap.put(3, "ウォーロード");
		classMap.put(4, "ヒューマン ナイト");
		classMap.put(5, "パラディン");
		classMap.put(6, "ダークアベンジャー");
		classMap.put(7, "ローグ");
		classMap.put(8, "トレジャー ハンター");
		classMap.put(9, "ホークアイ");
		classMap.put(10, "ヒューマン　メイジ");
		classMap.put(11, "ヒューマン ウィザード");
		classMap.put(12, "ソーサラー");
		classMap.put(13, "ネクロマンサー");
		classMap.put(14, "ウォーロック");
		classMap.put(15, "クレリック");
		classMap.put(16, "ビショップ");
		classMap.put(17, "プロフィット");
		classMap.put(18, "エルブンファイター");
		classMap.put(19, "エルブンナイト");
		classMap.put(20, "テンプルナイト");
		classMap.put(21, "ソードシンガー");
		classMap.put(22, "エルブンスカウト");
		classMap.put(23, "プレインズウォーカー");
		classMap.put(24, "シルバーレンジャー");
		classMap.put(25, "エルブンメイジ");
		classMap.put(26, "エルブンウィザード");
		classMap.put(27, "スペルシンガー");
		classMap.put(28, "エレメンタルサマナー");
		classMap.put(29, "オラクル");
		classMap.put(30, "エルダー");
		classMap.put(31, "ダークファイター");
		classMap.put(32, "パラスナイト");
		classMap.put(33, "シリエンナイト");
		classMap.put(34, "ブレードダンサー");
		classMap.put(35, "アサシン");
		classMap.put(36, "アビスウォーカー");
		classMap.put(37, "ファントムレンジャー");
		classMap.put(38, "ダーク メイジ");
		classMap.put(39, "ダークウィザード");
		classMap.put(40, "スペルハウラー");
		classMap.put(41, "ファントムサマナー");
		classMap.put(42, "シリエンオラクル");
		classMap.put(43, "シリエンエルダー");
		classMap.put(44, "オーク ファイター");
		classMap.put(45, "オークレイダー");
		classMap.put(46, "デストロイヤー");
		classMap.put(47, "オークモンク");
		classMap.put(48, "タイラント");
		classMap.put(49, "オーク メイジ");
		classMap.put(50, "オークシャーマン");
		classMap.put(51, "オーバーロード");
		classMap.put(52, "ウォークライヤー");
		classMap.put(53, "ドワーヴンファイター");
		classMap.put(54, "スカベンジャー");
		classMap.put(55, "バウンティーハンター");
		classMap.put(56, "アルティザン");
		classMap.put(57, "ウォースミス");
		classMap.put(88, "デュエリスト");
		classMap.put(89, "ドレッドノート");
		classMap.put(90, "フェニックスナイト");
		classMap.put(91, "ヘルナイト");
		classMap.put(92, "サジタリウス");
		classMap.put(93, "アドベンチャラー");
		classMap.put(94, "アークメイジ");
		classMap.put(95, "ソウルテイカー");
		classMap.put(96, "アルカナ ロード");
		classMap.put(97, "カーディナル");
		classMap.put(98, "ハイエロファント");
		classMap.put(99, "エヴァス テンプラー");
		classMap.put(100, "ソードミューズ");
		classMap.put(101, "ウィンドライダー");
		classMap.put(102, "ムーンライトセンティネル");
		classMap.put(103, "ミスティック ミューズ");
		classMap.put(104, "エレメンタルマスター");
		classMap.put(105, "エヴァス セイント");
		classMap.put(106, "シリエン テンプラー");
		classMap.put(107, "スペクトラルダンサー");
		classMap.put(108, "ゴースト ハンター");
		classMap.put(109, "ゴースト センティネル");
		classMap.put(110, "ストームスクリーマー");
		classMap.put(111, "スペクトラルマスター");
		classMap.put(112, "シリエン セイント");
		classMap.put(113, "タイタン");
		classMap.put(114, "グランド カバタリ");
		classMap.put(115, "ドミネーター");
		classMap.put(116, "ドゥーム クライヤー");
		classMap.put(117, "フォーチュン シーカー");
		classMap.put(118, "マエストロ");
		classMap.put(123, "カマエルソルジャー(男)");
		classMap.put(124, "カマエルソルジャー(女)");
		classMap.put(125, "トルーパー");
		classMap.put(126, "ウォーダー");
		classMap.put(127, "バーサーカー");
		classMap.put(128, "ソウルブレイカー(男)");
		classMap.put(129, "ソウルブレイカー(女)");
		classMap.put(130, "アヴァレスタ");
		classMap.put(131, "ドゥームブリンガー");
		classMap.put(132, "ソウルハウンド(男)");
		classMap.put(133, "ソウルハウンド(女)");
		classMap.put(134, "トリックスター");
		classMap.put(135, "インスペクター");
		classMap.put(136, "ジュディケーター");
		classMap.put(139, "フェニックスナイト（旧覚醒");
		classMap.put(140, "デュエリスト（旧覚醒");
		classMap.put(141, "アドベンチャラー（旧覚醒");
		classMap.put(142, "サジタリウス（旧覚醒");
		classMap.put(143, "アークメイジ（旧覚醒");
		classMap.put(144, "ジュディケーター（未実装");
		classMap.put(145, "アルカナ ロード（旧覚醒");
		classMap.put(146, "カーディナル（旧覚醒");
		classMap.put(148, "シーゲル フェニックスナイト");
		classMap.put(149, "シーゲル ヘルナイト");
		classMap.put(150, "シーゲル エヴァス テンプラー");
		classMap.put(151, "シーゲル シリエン テンプラー");
		classMap.put(152, "ティール デュエリスト");
		classMap.put(153, "ティール ドレッドノート");
		classMap.put(154, "ティール タイタン");
		classMap.put(155, "ティール グランド カバタリ");
		classMap.put(156, "ティール マエストロ");
		classMap.put(157, "イース ドゥームクライヤー");
		classMap.put(158, "オーセル アドベンチャラー");
		classMap.put(159, "オーセル ウィンドライダー");
		classMap.put(160, "オーセル ゴーストハンター");
		classMap.put(161, "オーセル フォーチュン シーカー");
		classMap.put(162, "ユール サジタリウス");
		classMap.put(163, "ユール ムーンライトセンティネル");
		classMap.put(164, "ユール ゴーストセンティネル");
		classMap.put(165, "ユール トリックスター");
		classMap.put(166, "フェオ アークメイジ");
		classMap.put(167, "フェオ ソウルテイカー");
		classMap.put(168, "フェオ ミスティックミューズ");
		classMap.put(169, "フェオ ストームスクリーマー");
		classMap.put(170, "フェオ ソウルハウンド（女");
		classMap.put(171, "イース ハイエロファント");
		classMap.put(172, "イース ソードミューズ");
		classMap.put(173, "イース スペクトラルダンサー");
		classMap.put(174, "イース ドミネーター");
		classMap.put(175, "イース ドゥームクライヤー");
		classMap.put(176, "ウィン アルカナ ロード");
		classMap.put(177, "ウィン エレメンタルマスター");
		classMap.put(178, "ウィン スペクトラルマスター");
		classMap.put(179, "エアロ カーディナル");
		classMap.put(180, "エアロ エヴァスセイント");
		classMap.put(181, "エアロ シリエンセイント");
		classMap.put(182, "アルテイア ファイター");
		classMap.put(183, "アルテイア ウィザード");
		classMap.put(184, "モラウド");
		classMap.put(185, "メナス");
		classMap.put(186, "ランカー");
		classMap.put(187, "ストーム メナス");
		classMap.put(188, "グラビティ ランカー");
		classMap.put(189, "サイハズ　シーアー");


		String ClassNameJP = classMap.get(classId);

		if (ClassNameJP == null)
		{
			throw new IllegalArgumentException("No template for classId: " + classId);
		}
		return ClassNameJP;
	}
	
	public final String getClassTypeJPById(int classId)
	{
		switch(classId){
			case 0:  return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 1:  return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 2:  return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 3:  return "<font color=\"CC99FF\">POL 　　槍　</font>";
			case 4:  return "<font color=\"99FF33\">POL 　　盾　</font>";
			case 5:  return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 6:  return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 7:  return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 8:  return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 9:  return "<font color=\"FFFF33\">BOW 　　弓　</font>";
			case 10: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 11: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 12: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 13: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 14: return "<font color=\"CC0000\">SLM 　サモナ</font>";
			case 15: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 16: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 17: return "<font color=\"FF9933\">HEL 　バ　フ</font>";
			case 18: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 19: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 20: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 21: return "<font color=\"FF9933\">SLD 　　歌　</font>";
			case 22: return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 23: return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 24: return "<font color=\"FFFF33\">BOW 　　弓　</font>";
			case 25: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 26: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 27: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 28: return "<font color=\"CC0000\">SLM 　サモナ</font>";
			case 29: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 30: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 31: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 32: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 33: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 34: return "<font color=\"FF9933\">DNS 　　踊　</font>";
			case 35: return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 36: return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 37: return "<font color=\"FFFF33\">BOW 　　弓　</font>";
			case 38: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 39: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 40: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 41: return "<font color=\"CC0000\">SLM 　サモナ</font>";
			case 42: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 43: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 44: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 45: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 46: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 47: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 48: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 49: return "<font color=\"FF9933\">BFF 　バ　フ</font>";
			case 50: return "<font color=\"FF9933\">BFF 　バ　フ</font>";
			case 51: return "<font color=\"FF9933\">OVL 　オバロ</font>";
			case 52: return "<font color=\"FF9933\">BFF 　バ　フ</font>";
			case 53: return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 54: return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 55: return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 56: return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 57: return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 88: return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 89: return "<font color=\"CC99FF\">POL 　　槍　</font>";
			case 90: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 91: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 92: return "<font color=\"FFFF33\">BOW 　　弓　</font>";
			case 93: return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 94: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 95: return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 96: return "<font color=\"CC0000\">SLM 　サモナ</font>";
			case 97: return "<font color=\"3366FF\">SLM 　ヒ　ラ</font>";
			case 98: return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 99: return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 100:return "<font color=\"FF9933\">SNG 　　歌　</font>";
			case 101:return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 102:return "<font color=\"FFFF33\">BOW 　　弓　</font>";
			case 103:return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 104:return "<font color=\"CC0000\">SLM 　サモナ</font>";
			case 105:return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 106:return "<font color=\"99FF33\">SLD 　　盾　</font>";
			case 107:return "<font color=\"FF9933\">DNS 　踊　り</font>";
			case 108:return "<font color=\"B4B4B4\">DGR 　短　剣</font>";
			case 109:return "<font color=\"FFFF33\">BOW 　　弓　</font>";
			case 110:return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 111:return "<font color=\"CC0000\">SLM 　サモナ</font>";
			case 112:return "<font color=\"3366FF\">HEL 　ヒ　ラ</font>";
			case 113:return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 114:return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 115:return "<font color=\"FF9933\">OVL 　オバロ</font>";
			case 116:return "<font color=\"FF9933\">BFF 　バ　フ</font>";
			case 117:return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 118:return "<font color=\"FFFFFF\">DWF 　ド　ワ</font>";
			case 123:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 124:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 125:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 126:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 127:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 128:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 129:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 130:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 131:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 132:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 133:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 134:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 135:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 136:return "<font color=\"00FFFF\">KML 　カ　マ</font>";
			case 139:return "<font color=\"99FF33\">OSLD　　盾　</font>";
			case 140:return "<font color=\"CC0000\">OATK　近　接</font>";
			case 141:return "<font color=\"B4B4B4\">ODGR　短　剣</font>";
			case 142:return "<font color=\"FFFF33\">OBOW　　弓　</font>";
			case 143:return "<font color=\"FFFF33\">OWIZ　ＷＩＺ</font>";
			case 144:return "<font color=\"00FFFF\">OKML　カ　マ</font>";
			case 145:return "<font color=\"CC0000\">OSLM　サモナ</font>";
			case 146:return "<font color=\"3366FF\">OSLM　ヒ　ラ</font>";
			case 148:return "<font color=\"99FF33\">ASLD　　盾　</font>";
			case 149:return "<font color=\"99FF33\">ASLD　　盾　</font>";
			case 150:return "<font color=\"99FF33\">ASLD　　盾　</font>";
			case 151:return "<font color=\"99FF33\">ASLD　　盾　</font>";
			case 152:return "<font color=\"CC0000\">AATK　近　接</font>";
			case 153:return "<font color=\"CC99FF\">APOL　　槍　</font>";
			case 154:return "<font color=\"CC0000\">AATK　近　接</font>";
			case 155:return "<font color=\"CC0000\">AATK　近　接</font>";
			case 156:return "<font color=\"CC0000\">ADWF　近　接</font>";
			case 157:return "<font color=\"FF9933\">ABFF　バ　フ</font>";
			case 158:return "<font color=\"B4B4B4\">ADGR　短　剣</font>";
			case 159:return "<font color=\"B4B4B4\">ADGR　短　剣</font>";
			case 160:return "<font color=\"B4B4B4\">ADGR　短　剣</font>";
			case 161:return "<font color=\"FFFFFF\">ADWF　ド　ワ</font>";
			case 162:return "<font color=\"FFFF33\">ABOW　　弓　</font>";
			case 163:return "<font color=\"FFFF33\">ABOW　　弓　</font>";
			case 164:return "<font color=\"FFFF33\">ABOW　　弓　</font>";
			case 165:return "<font color=\"00FFFF\">AKML　カ　マ</font>";
			case 166:return "<font color=\"FFFF33\">AWIZ　ＷＩＺ</font>";
			case 167:return "<font color=\"FFFF33\">AWIZ　ＷＩＺ</font>";
			case 168:return "<font color=\"FFFF33\">AWIZ　ＷＩＺ</font>";
			case 169:return "<font color=\"FFFF33\">AWIZ　ＷＩＺ</font>";
			case 170:return "<font color=\"00FFFF\">AKML　カ　マ</font>";
			case 171:return "<font color=\"3366FF\">AHEL　ヒ　ラ</font>";
			case 172:return "<font color=\"FF9933\">ASNG　バ　フ</font>";
			case 173:return "<font color=\"FF9933\">ADNS　バ　フ</font>";
			case 174:return "<font color=\"FF9933\">AOVL　オバロ</font>";
			case 175:return "<font color=\"FF9933\">ABFF　バ　フ</font>";
			case 176:return "<font color=\"CC0000\">ASLM　サモナ</font>";
			case 177:return "<font color=\"CC0000\">ASLM　サモナ</font>";
			case 178:return "<font color=\"CC0000\">ASLM　サモナ</font>";
			case 179:return "<font color=\"3366FF\">ASLM　ヒ　ラ</font>";
			case 180:return "<font color=\"3366FF\">ASLM　ヒ　ラ</font>";
			case 181:return "<font color=\"3366FF\">AHEL　ヒ　ラ</font>";
			case 182:return "<font color=\"CC0000\">ETF 　近　接</font>";
			case 183:return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 184:return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 185:return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 186:return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 187:return "<font color=\"FFFF33\">WIZ 　ＷＩＺ</font>";
			case 188:return "<font color=\"CC0000\">ATK 　近　接</font>";
			case 189:return "<font color=\"CC0000\">ATK 　近　接</font>";
		}
		return "?";
	}
	
	/**
	 * Gets the single instance of ClassListData.
	 * @return single instance of ClassListData
	 */
	public static ClassListData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final ClassListData _instance = new ClassListData();
	}
}