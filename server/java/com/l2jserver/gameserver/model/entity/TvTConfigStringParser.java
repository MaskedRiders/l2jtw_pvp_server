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
public class TvTConfigStringParser
{	
	private static final Logger _log = Logger.getLogger(TvTConfigStringParser.class.getName());
	public static Map<Integer, TvTPattern> _patterns;
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
	
	
	public static TvTPattern getCurrentPattern(){
		return _patterns.get(_currentId);
	}
	public static void parseXMLNodes(Node rootNode)
	{
		_patterns = new HashMap<Integer,TvTPattern>();
		for (Node listNode = rootNode.getFirstChild(); listNode != null; listNode = listNode.getNextSibling())
		{
			if (listNode.getNodeName().equals("list"))
			{
				for (Node n = listNode.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if ("pattern".equalsIgnoreCase(n.getNodeName()))
					{
						_log.warning("pattern");
						_currentId = _id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
						_name      = n.getAttributes().getNamedItem("name").getNodeValue();
						parseTvTPatterns(n);
						TvTPattern hogehoge = new TvTPattern(
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
						);
						_patterns.put(_currentId, hogehoge);

					}
				}
			}
		}
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
	
	private static void parseTvTEvents(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("TvTEventInstanceFile".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue();
				_TvTEventInstanceFile = n.getAttributes().getNamedItem("value").getNodeValue();
			}
			if ("TvTEventParticipationNpcId".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue();
				_TvTEventParticipationNpcId = Integer.parseInt(r);
				
			}
			if ("TvTEventParticipationFee".equalsIgnoreCase(n.getNodeName())){
				_log.warning("==TvTEventParticipationFee Start==");
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue();
				_TvTEventParticipationFee = split2Item(n.getAttributes().getNamedItem("value").getNodeValue());
			}
			if ("TvTEventParticipationNpcCoordinates".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventParticipationNpcCoordinates = splitCordinate(n.getAttributes().getNamedItem("value").getNodeValue());
			}
			if ("TvTEventReward".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventReward = splitItemList(n.getAttributes().getNamedItem("value").getNodeValue());
			}
			if ("TvTDoorsToOpen".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTDoorsToOpen = splitIdList(n.getAttributes().getNamedItem("value").getNodeValue());
			}
			if ("TvTDoorsToClose".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTDoorsToClose = splitIdList(n.getAttributes().getNamedItem("value").getNodeValue());
			}
		}
	}
	
	private static void parseTvTEventTeams(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("TvTEventTeam1Name".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventTeam1Name = n.getAttributes().getNamedItem("value").getNodeValue();
			}
			if ("TvTEventTeam1Coordinates".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventTeam1Coordinates = splitCordinate(n.getAttributes().getNamedItem("value").getNodeValue());
			}
			if ("TvTEventTeam2Name".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventTeam2Name = n.getAttributes().getNamedItem("value").getNodeValue();
			}
			if ("TvTEventTeam2Coordinates".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventTeam2Coordinates = splitCordinate(n.getAttributes().getNamedItem("value").getNodeValue());
			}
		}
	}
	
	private static void parseTvTManagers(Node rootNode)
	{
		for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("TvTEventParticipationTime".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventParticipationTime = Integer.parseInt(r);
			}
			if ("TvTEventMeetingTime".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventMeetingTime = Integer.parseInt(r);
			}
			if ("TvTEventRunningTime".equalsIgnoreCase(n.getNodeName())){
				NamedNodeMap attr = n.getAttributes();
				Node item = attr.getNamedItem("value");
				String r = item.getNodeValue()	;
				_TvTEventRunningTime = Integer.parseInt(r);
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
				_log.warning("TvTEventEngine[TvTConfigParser.splitBuffHash(value)]: no buffAray -> buffAray.length == 0");
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
				_log.warning("TvTEventEngine[TvTConfigParser.splitItemList(value)]: no itemAray -> itemAray.length == 0");
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
				_log.warning("TvTEventEngine[TvTConfigParser.splitIdList(value)]: invalid parse items -> " + nfe.toString());
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
			_log.warning("TvTEventEngine[TvTConfigParser.split2Item(value)]: invalid parse items -> " + nfe.toString());
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
			_log.warning("TvTEventEngine[TvTConfigParser.splitCordinates(value)]: invalid parse coords -> " + nfe.toString());
		}		
		return result;
	}
	
	/* TvTPattern構造体 */
	static class TvTPattern
	{
		public int                   id;
		public String                name;
		
		public String                TvTEventInstanceFile;
		public int                   TvTEventParticipationNpcId;
		public int[]                 TvTEventParticipationFee;
		public int[]                 TvTEventParticipationNpcCoordinates;
		public List<int[]>           TvTEventReward;
		public List<Integer>         TvTDoorsToOpen;
		public List<Integer>         TvTDoorsToClose;

		public String                TvTEventTeam1Name;
		public int[]                 TvTEventTeam1Coordinates;
		public String                TvTEventTeam2Name;
		public int[]                 TvTEventTeam2Coordinates;

		public int                   TvTEventParticipationTime;
		public int                   TvTEventMeetingTime;
		public int                   TvTEventRunningTime;
		
		public TvTPattern(){}
		public TvTPattern(int inputId,
				String inputName,
				String inputTvTEventInstanceFile,
				int inputTvTEventParticipationNpcId,
				int[] inputTvTEventParticipationFee,
				int[] inputTvTEventParticipationNpcCoordinates,
				List<int[]> inputTvTEventReward,
				List<Integer> inputTvTDoorsToOpen,
				List<Integer> inputTvTDoorsToClose,
				String inputTvTEventTeam1Name,
				int[] inputTvTEventTeam1Coordinates,
				String inputTvTEventTeam2Name,
				int[] inputTvTEventTeam2Coordinates,
				int inputTvTEventParticipationTime,
				int inputTvTEventMeetingTime,
				int inputTvTEventRunningTime)
		{
			id                                        = inputId;
			name                                      = inputName;
			TvTEventInstanceFile                      = inputTvTEventInstanceFile;
			TvTEventParticipationNpcId                = inputTvTEventParticipationNpcId;
			TvTEventParticipationFee                  = inputTvTEventParticipationFee;
			TvTEventParticipationNpcCoordinates       = inputTvTEventParticipationNpcCoordinates;
			TvTEventReward                            = inputTvTEventReward;
			TvTDoorsToOpen                            = inputTvTDoorsToOpen;
			TvTDoorsToClose                           = inputTvTDoorsToClose;
			TvTEventTeam1Name                         = inputTvTEventTeam1Name;
			TvTEventTeam1Coordinates                  = inputTvTEventTeam1Coordinates;
			TvTEventTeam2Name                         = inputTvTEventTeam2Name;
			TvTEventTeam2Coordinates                  = inputTvTEventTeam2Coordinates;
			TvTEventParticipationTime                 = inputTvTEventParticipationTime;
			TvTEventMeetingTime                       =inputTvTEventMeetingTime;
			TvTEventRunningTime                       = inputTvTEventRunningTime;
		}
	}

}
