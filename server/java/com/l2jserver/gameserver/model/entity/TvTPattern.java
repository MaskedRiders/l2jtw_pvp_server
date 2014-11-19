package com.l2jserver.gameserver.model.entity;
import java.util.List;
	
/* TvTPattern構造体 */
public class TvTPattern
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