package com.l2jserver.gameserver.model.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author MaskedRiderW
 */
public class TvTConfigParser
{	
	private static final Logger _log = Logger.getLogger(TvTConfigParser.class.getName());
	public static int                      _currentId;

	private static String                  _name;
	private static int                     _id;
	   
	private static String                  _TvTEventInstanceFile;
	private static int                     _TvTEventParticipationNpcId;
	private static int[]                   _TvTEventParticipationFee;
	private static int[]                   _TvTEventParticipationNpcCoordinates;
	private static List<int[]>             _TvTEventReward;
	private static List<Integer>           _TvTDoorsToOpen;
	private static List<Integer>           _TvTDoorsToClose;
	
	private static String                  _TvTEventTeam1Name;
	private static int[]                   _TvTEventTeam1Coordinates;
	private static String                  _TvTEventTeam2Name;
	private static int[]                   _TvTEventTeam2Coordinates;
	    
	private static int                     _TvTEventParticipationTime;
	private static int                     _TvTEventMeetingTime;
	private static int                     _TvTEventRunningTime;
	
	public static Map<Integer, TvTPattern> parseXMLNodes(Node rootNode)
	{
		 HashMap<Integer, TvTPattern> patterns = new HashMap<>();
		 
		for (Node listNode = rootNode.getFirstChild(); listNode != null; listNode = listNode.getNextSibling())
		{
			if (listNode.getNodeName().equals("list"))
			{
				for (Node n = listNode.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if ("pattern".equalsIgnoreCase(n.getNodeName()))
					{
						_currentId = _id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
						_name      = n.getAttributes().getNamedItem("name").getNodeValue();

						// XMLのノードをパース
						parseTvTPatterns(n);
						// コンテナにパターンをプッシュ
						patterns.put(_currentId, new TvTPattern(
								_id,
								_name,
								_TvTEventInstanceFile,
								_TvTEventParticipationNpcId,
								_TvTEventParticipationFee,
								_TvTEventParticipationNpcCoordinates,
								_TvTEventReward,
								_TvTDoorsToOpen,
								_TvTDoorsToClose,
								_TvTEventTeam1Name,
								_TvTEventTeam1Coordinates,
								_TvTEventTeam2Name,
								_TvTEventTeam2Coordinates,
								_TvTEventParticipationTime,
								_TvTEventMeetingTime,
								_TvTEventRunningTime
						));
					}
				}
			}
		}
		return patterns;
	}

	private static void parseTvTPatterns(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("tvtEvent".equalsIgnoreCase(n.getNodeName()))
			{
				parseTvTEvents(n);
			}
			else if ("tvtEventTeam".equalsIgnoreCase(n.getNodeName()))
			{
				parseTvTEventTeams(n);
			}
			else if ("tvtManager".equalsIgnoreCase(n.getNodeName()))
			{
				parseTvTManagers(n);
			}
		}
	}
	
	private static String getNodeAValue(Node n){
		try{
			NamedNodeMap attr = n.getAttributes();
			Node item = attr.getNamedItem("value");
			return item.getNodeValue();
		}
		catch(Exception e){
			_log.warning("TvTEventEngine[TvTConfigStringParser.getNodeAValue(Node n)]: no Value ->"+ n.getNodeName());
		}
		return "";
	}
	
	private static void parseTvTEvents(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("TvTEventInstanceFile".equalsIgnoreCase(n.getNodeName())){
				_TvTEventInstanceFile = getNodeAValue(n);
			}
			if ("TvTEventParticipationNpcId".equalsIgnoreCase(n.getNodeName())){
				_TvTEventParticipationNpcId = Integer.parseInt(getNodeAValue(n));
			}
			if ("TvTEventParticipationFee".equalsIgnoreCase(n.getNodeName())){
				_TvTEventParticipationFee = split2Item(getNodeAValue(n));
			}
			if ("TvTEventParticipationNpcCoordinates".equalsIgnoreCase(n.getNodeName())){
				_TvTEventParticipationNpcCoordinates = splitCordinate(getNodeAValue(n));
			}
			if ("TvTEventReward".equalsIgnoreCase(n.getNodeName())){
				_TvTEventReward = splitItemList(getNodeAValue(n));
			}
			if ("TvTDoorsToOpen".equalsIgnoreCase(n.getNodeName())){
				_TvTDoorsToOpen = splitIdList(getNodeAValue(n));
			}
			if ("TvTDoorsToClose".equalsIgnoreCase(n.getNodeName())){
				_TvTDoorsToClose = splitIdList(getNodeAValue(n));
			}
		}
	}
	
	private static void parseTvTEventTeams(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("TvTEventTeam1Name".equalsIgnoreCase(n.getNodeName())){
				_TvTEventTeam1Name = getNodeAValue(n);
			}
			if ("TvTEventTeam1Coordinates".equalsIgnoreCase(n.getNodeName())){
				_TvTEventTeam1Coordinates = splitCordinate(getNodeAValue(n));
			}
			if ("TvTEventTeam2Name".equalsIgnoreCase(n.getNodeName())){
				_TvTEventTeam2Name = getNodeAValue(n);
			}
			if ("TvTEventTeam2Coordinates".equalsIgnoreCase(n.getNodeName())){
				_TvTEventTeam2Coordinates = splitCordinate(getNodeAValue(n));
			}
		}
	}
	
	private static void parseTvTManagers(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("TvTEventParticipationTime".equalsIgnoreCase(n.getNodeName())){
				_TvTEventParticipationTime = Integer.parseInt(getNodeAValue(n));
			}
			if ("TvTEventMeetingTime".equalsIgnoreCase(n.getNodeName())){
				_TvTEventMeetingTime = Integer.parseInt(getNodeAValue(n));
			}
			if ("TvTEventRunningTime".equalsIgnoreCase(n.getNodeName())){
				_TvTEventRunningTime = Integer.parseInt(getNodeAValue(n));
			}
		}
	}
	
	public static Map<Integer, Integer> splitBuffHash(String stringValue){
		if(stringValue.equals("")) return new HashMap<>(0);
		String[] buffs = stringValue.split(";");
		Map<Integer, Integer> result = new HashMap<>(buffs.length);
		int[] buffAray;
		for (String buff : buffs){
			buffAray = split2Item(buff);
			if(buffAray.length == 0){
				_log.warning("TvTEventEngine[TvTConfigStringParser.splitBuffHash(value)]: no buffAray -> buffAray.length == 0");
				return result;
			}
			result.put(buffAray[0], buffAray[1]);
		}
		return result;
	}
	
	public static List<int[]> splitItemList(String stringValue){
		if(stringValue.equals("")) return new ArrayList<>();
		List<int[]> result = new ArrayList<>();
		int[] itemAray;
		String[] items = stringValue.split(";");
		for (String item : items){
			itemAray = split2Item(item);
			if(itemAray.length == 0){
				_log.warning("TvTEventEngine[TvTConfigStringParser.splitItemList(value)]: no itemAray -> itemAray.length == 0");
				return result;
			}
			result.add(itemAray);
		}
		return result;
	}
	
	public static List<Integer> splitIdList(String stringValue){
		if(stringValue.equals("")) return new ArrayList<>();
		List<Integer> result = new ArrayList<>();
		String[] ids = stringValue.split(";");
		_log.warning("ids.length==>"+ids.length);
		for (String id : ids){
			try{
				_log.warning("id==>"+id);
				result.add(Integer.parseInt(id));
			}
			catch(NumberFormatException nfe){
				_log.warning("TvTEventEngine[TvTConfigStringParser.splitIdList(value)]: invalid parse items -> " + nfe.toString());
				return result;
			}
		}
		return result;
	}

	public static int[] split2Item(String stringValue){
		if(stringValue.equals("")) return new int[0];
		String[] fee = stringValue.split(",");
		int[] result = new int[2];
		if (fee.length < 2)
		{
			return result;
		}
		try
		{
			result[0] = Integer.parseInt(fee[0]);
			result[1] = Integer.parseInt(fee[1]);
		}
		catch (NumberFormatException nfe)
		{
			_log.warning("TvTEventEngine[TvTConfigStringParser.split2Item(value)]: invalid parse items -> " + nfe.toString());
		}		

		return result;
	}
	
	/*
	* 異常があると空配列を返す
	*/
	public static int[] splitCordinate(String stringValue){
		String[] coords = stringValue.split(",");
		int[] result = new int[4];
		if (coords.length < 3)
		{
			return result;
		}
		try
		{
			result[0] = Integer.parseInt(coords[0]);
			result[1] = Integer.parseInt(coords[1]);
			result[2] = Integer.parseInt(coords[2]);
			if (coords.length == 4)
			{
				result[3] = Integer.parseInt(coords[3]);
			}
		}
		catch (NumberFormatException nfe)
		{
			_log.warning("TvTEventEngine[TvTConfigStringParser.splitCordinates(value)]: invalid parse coords -> " + nfe.toString());
		}		
		return result;
	}
}
