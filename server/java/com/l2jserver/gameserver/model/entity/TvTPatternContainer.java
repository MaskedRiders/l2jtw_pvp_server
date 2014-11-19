package com.l2jserver.gameserver.model.entity;

import com.l2jserver.Config;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * TvTのパターンを入れるコンテナクラス
 * @author MaskedRiderW
 */
public class TvTPatternContainer
{	
	protected static final Logger _log = Logger.getLogger(TvTPatternContainer.class.getName());
	public static Map<Integer, TvTPattern> _patterns;
	public static int                      _currentId;

	public static void init(){
		// TvTのパターンを読み込む
		File xml = new File(Config.DATAPACK_ROOT, "/data/tvtPatterns.xml");
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			if (!xml.exists())
			{
				_log.severe("[TvTPatternContainer] Missing tvtPattern.xml.");
				return;
			}
			factory.setValidating(false); // バリデーション無視
			factory.setIgnoringComments(true); // コメント無視
		// コンテアナの全パターンを初期化
			_patterns.clear();
			_patterns = new HashMap<Integer, TvTPattern>();
			_patterns = TvTConfigParser.parseXMLNodes(factory.newDocumentBuilder().parse(xml));
		}
		catch (IOException e)
		{
			_log.log(Level.WARNING, "Instance: can not find " + xml.getAbsolutePath() + " ! " + e.getMessage(), e);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Instance: error while loading " + xml.getAbsolutePath() + " ! " + e.getMessage(), e);
		}
	}
	
	/**
	 *
	 * @return
	 */
	public static TvTPattern getCurrentPattern(){
		return _patterns.get(_currentId);
	}

	/**
	 * カレントＩＤをシャッフル
	 */
	public static void doCurrentIdShuffle(){
		Random rnd = new Random();
		Integer[] keys = _patterns.keySet().toArray(new Integer[_patterns.size()]);
		_currentId = keys[rnd.nextInt(keys.length)];
	}

}
