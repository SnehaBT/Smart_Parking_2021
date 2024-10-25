from tkinter import Tk, Label, Entry, Button
# import MySQLdb
import numpy as np
import cv2
import time

import os
sdThresh = 50
font = cv2.FONT_HERSHEY_SIMPLEX
sid=1
#TODO: Face Detection 1
def distMap(frame1, frame2):
    """outputs pythagorean distance between two frames"""
    frame1_32 = np.float32(frame1)
    frame2_32 = np.float32(frame2)
    diff32 = frame1_32 - frame2_32
    norm32 = np.sqrt(diff32[:,:,0]**2 + diff32[:,:,1]**2 + diff32[:,:,2]**2)/np.sqrt(255**2 + 255**2 + 255**2)
    dist = np.uint8(norm32*255)
    return dist
cv2.namedWindow('frame')
cv2.namedWindow('dist')
#capture video stream from camera source. 0 refers to first camera, 1 referes to 2nd and so on.
cap = cv2.VideoCapture(0)

_, frame1 = cap.read()

facecount = 0
while(True):
    _, frame3 = cap.read()

    rows, cols, _ = np.shape(frame3)
    cv2.imshow('dist', frame3)
    dist = distMap(frame1, frame3)

    # apply Gaussian smoothing
    mod = cv2.GaussianBlur(dist, (9,9), 0)
    # apply thresholding
    _, thresh = cv2.threshold(mod, 100, 255, 0)
    # calculate st dev test
    _, stDev = cv2.meanStdDev(mod)
    cv2.imshow('dist', mod)
    cv2.putText(frame1, "Standard Deviation - {}".format(round(stDev[0][0],0)), (70, 70), font, 1, (255, 0, 255), 1, cv2.LINE_AA)
    a = stDev



    if stDev > sdThresh:
        from datetime import datetime
        import pymysql
        print("============================")

        con = pymysql.connect(host='localhost', port=3306, user='root', password='', db='smart_parking')
        cmd = con.cursor()
        cmd.execute("SELECT * FROM `slot` WHERE `slot_id`=48")
        res=cmd.fetchone()
        print(res)
        if res is not None:
            if res[3]!='free':
                cmd.execute("SELECT `booking`.`booking_id` FROM `booking` WHERE `status`='entered' AND `slot_id`='48'")
                s=cmd.fetchone()
                print(s)
                if s is not None:
                    fn=datetime.now().strftime("%Y%m%d_%H%M%S")+".jpg"
                    cv2.imwrite("static/alert/"+fn,frame3)
                    cmd.execute("INSERT INTO `alert` VALUES(NULL,'"+str(s[0])+"',NOW(),'"+fn+"','pending')")
                    con.commit()




                print("diff..",stDev);

    cv2.imshow('dist', frame3)
    cv2.imshow('frame', frame1)
    if cv2.waitKey(1) & 0xFF == 27:
        break

cap.release()
cv2.destroyAllWindows()




