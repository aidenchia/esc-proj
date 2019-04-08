# -*- coding: utf-8 -*-
"""
Created on Tue Feb 19 14:53:33 2019

@author: Tan You
"""

from flask import Flask, render_template
import json, pandas as pd
from pandas import ExcelWriter
from pandas import ExcelFile


app = Flask(__name__)

term5coh1 = {
'mon': {'8:30-9:00':'50.034', '9:00-9:30':'50.034', '9:30-10:00':'50.034', '10:00-10:30':'50.005', 
		'10:30-11:00':'50.005', '11:00-11:30':'50.005', '11:30-12:00':'50.003', '12:00-12:30':'50.003',
		'12:30-13:00':'50.003', '13:00-13:30':'null', '13:30-14:00':'null', '14:00-14:30':'null',
		'14:30-15:00':'null', '15:00-15:30':'hass', '15:30-16:00':'hass', '16:30-17:00':'hass',
		'17:00-17:30':'hass', '17:30-18:00':'hass', '18:00-18:30':'hass'}, 
'tue': {'8:30-9:00':'hass', '9:00-9:30':'hass', '9:30-10:00':'hass', '10:00-10:30':'null', 
		'10:30-11:00':'null', '11:00-11:30':'null', '11:30-12:00':'50.034', '12:00-12:30':'50.034',
		'12:30-13:00':'50.034', '13:00-13:30':'null', '13:30-14:00':'null', '14:00-14:30':'null',
		'14:30-15:00':'null', '15:00-15:30':'null', '15:30-16:00':'50.003', '16:00-16:30':'50.003',
		'17:00-17:30':'50.003', '17:30-18:00':'null', '18:00-18:30':'null'}, 
'wed': {'8:30-9:00':'null', '9:00-9:30':'50.005', '9:30-10:00':'50.005', '10:00-10:30':'50.005', 
		'10:30-11:00':'null', '11:00-11:30':'null', '11:30-12:00':'null', '12:00-12:30':'null',
		'12:30-13:00':'null', '13:00-13:30':'null', '13:30-14:00':'null', '14:00-14:30':'null',
		'14:30-15:00':'null', '15:00-15:30':'null', '15:30-16:00':'null', '16:00-16:30':'null',
		'17:00-17:30':'null', '17:30-18:00':'null', '18:00-18:30':'null'}, 
'thu': {'8:30-9:00':'50.034', '9:00-9:30':'50.034', '9:30-10:00':'50.034', '10:00-10:30':'50.034', 
		'10:30-11:00':'50.005', '11:00-11:30':'50.005', '11:30-12:00':'50.005', '12:00-12:30':'50.005',
		'12:30-13:00':'null', '13:00-13:30':'null', '13:30-14:00':'null', '14:00-14:30':'null',
		'14:30-15:00':'null', '15:00-15:30':'hass', '15:30-16:00':'hass', '16:00-16:30':'hass',
		'17:00-17:30':'hass', '17:30-18:00':'hass', '18:00-18:30':'hass'}, 
'fri': {'8:30-9:00':'50.003', '9:00-9:30':'50.003', '9:30-10:00':'50.003', '10:00-10:30':'50.003', 
		'10:30-11:00':'null', '11:00-11:30':'null', '11:30-12:00':'null', '12:00-12:30':'null',
		'12:30-13:00':'50.005', '13:00-13:30':'50.005', '13:30-14:00':'50.003', '14:00-14:30':'null',
		'14:30-15:00':'null', '15:00-15:30':'null', '15:30-16:00':'null', '16:00-16:30':'null',
		'17:00-17:30':'null', '17:30-18:00':'null', '18:00-18:30':'null'}}

json_send = json.dumps(term5coh1)

@app.route("/GetData")
def GetData():
    """
    Do not delete the commented out code until submission at end of term
    """
    """
    df = pd.read_excel('sample_input_format.xlsx',sheetname='ISTD')
    temp= df.to_dict('records')
    columnNames = df.columns.values
    return render_template('testjsondisplay.html',records=temp, colnames = columnNames)
    """
    """
    json_retrived = json.loads(json_send)
    term5coh1_inverted = dict()
    for k, v in json_retrived.items():
        for k1, v2 in v.items():
            term5coh1_inverted.setdefault(k1,dict())[k] = v2
    """
    term5coh1_inverted = dict()
    for k, v in term5coh1.items():
        previous_lesson = None
        rowspan_count = 1
        previous_time = '8:30-9:00'
        for k1, v2 in v.items():
            #print('yes')
            if previous_lesson == v2:
                rowspan_count+=1
                term5coh1_inverted[previous_time][k][1] = rowspan_count
                term5coh1_inverted.setdefault(k1,dict())[k] = [v2,0]
            else:
                previous_lesson = v2
                rowspan_count = 1
                previous_time = k1
                term5coh1_inverted.setdefault(k1,dict())[k] = [v2,rowspan_count]
    return render_template('testjsondisplay.html',json_r=term5coh1_inverted)

if __name__ == "__main__":
    app.run(host='127.0.0.1', port=8000)
