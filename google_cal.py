import json
import time
import datetime

weekday = {'Mon':0, 'Tue':1, 'Wed':2, 'Thu':3, 'Fri':4, 'Sat':5, 'Sun':6}
day_slot = {0: '08:30:00', 1: '09:00:00',
			2: '09:30:00', 3: '10:00:00',
			4: '10:30:00', 5: '11:00:00',
			6: '11:30:00', 7: '12:00:00',
			8: '12:30:00', 9: '13:00:00',
			10: '13:30:00', 11: '14:00:00',
			12: '14:30:00', 13: '15:00:00',
			14: '15:30:00', 15: '16:00:00',
			16: '16:30:00', 17: '17:00:00',
			18: '17:30:00', 19: '18:00:00',
			20: '18:30:00', 21: '19:00:00'}

def genrate_ics(sClassSet, role, id): # role = {studentG, prof}
	google_dic = {}
	for i in sClassSet:
		if id in i[role]:
			google_dic['summary'] = i['subject']
			google_dic['location'] = i['classroom']
			start_time_dic = {}
			start_time_dic['dateTime'] = '2019-04-28T09:00:00-07:00'
			start_time_dic['timeZone'] = 'America/Los_Angeles'
			google_dic['start'] = start_time_dic
			end_time_dic = {}
			end_time_dic['dateTime'] = '2019-04-28T10:00:00-07:00'
			end_time_dic['timeZone'] = 'America/Los_Angeles'
			google_dic['end'] = start_time_dic
	return google_dic
#			e = Event()
#			now = datetime.datetime.now()
#
#			name = str(i['subject']) +' ' + i['classroom']
#			year = time.strftime("%Y", time.localtime())
#			month = time.strftime("%m", time.localtime())
#			today = time.strftime("%d", time.localtime())
#			today_weekday = weekday[time.strftime("%a", time.localtime())]
#			classday_whole = now + datetime.timedelta(((-today_weekday+i['weekday'])))
#			classday = classday_whole.strftime('%d')
#			start_time = day_slot[i['startTime']]
#				
#			ics_start = year+month+classday+'T'+ start_time + '+08:00'
#			duration = i['duration']
#			e.name = name
#			e.begin = ics_start
#			e.duration = {'days':0, 'hours':duration}
#			calendar.events.add(e)		
	
google_dict = {}
with open("timetable.json",'r') as load_f:
	load_dict = json.load(load_f)
	
	sClassSet = load_dict['specific class']
	google_dict = genrate_ics(sClassSet, 'professor', 0)
	
with open('test.json', 'w') as f:
	json_str = json.dumps(google_dict)
	f.writelines(json_str)		
	
	
