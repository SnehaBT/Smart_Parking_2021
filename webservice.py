import flask
import pymysql

con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='smart_parking')
cmd=con.cursor()
from flask import Flask,render_template,request,session,make_response,jsonify
from werkzeug.utils import secure_filename
import os
import json
app=Flask(__name__)
@app.route('/login',methods=['get','post'])
def login():
    name=request.form['un']
    pword=request.form['ps']
    cmd.execute("select * from login where username='"+str(name)+"' and password='"+str(pword)+"' ")
    s=cmd.fetchone()
    print(s)
    if s==None:
        return jsonify({'result':'error'})
    else:
        return jsonify({'result':str(s[0])})

@app.route('/signup',methods=['post','get'])
def signup():
    try:
        print(request.form)
        fn = request.form['firstname']
        mn=request.form['midn']
        ln=request.form['lsn']

        dob = request.form['dob']

        gender = request.form['gender']


        phno = request.form['phone']

        email = request.form['email']

        un = request.form['un']
        print(un)
        pw = request.form['pw']
        print(pw)
        cmd.execute("insert into login values(NULL,'"+un+"','"+pw+"','user')")
        id = con.insert_id()
        cmd.execute("insert into user_registration values(NULL,'"+str(id)+"','"+fn+"','"+mn+"','"+ln+"','"+gender+"','"+dob+"','"+email+"','"+phno+"')")
        con.commit()
        return jsonify({'task':"success"})
    except Exception as e:
        print(str(e))
        return jsonify({'task': "data already exist"})
@app.route('/booking', methods=['post', 'get'])
def booking():
    try:
        uid=request.form['uid']
        sid=request.form['sid']
        cmd.execute("insert into booking values(NULL,"+uid+","+sid+",now(),'pending')")
        con.commit()
        return jsonify({'task':"success"})
    except Exception as e:
        print(str(e))
        return jsonify({'task': "Faild"})

@app.route('/alertstatus', methods=['post'])
def alertstatus():

            aid = request.form['aid']
            print(aid,"aiiid")
            cmd.execute("update alert set status='viewed' where alert_id='"+str(aid)+"'")
            con.commit()
            return jsonify({'task': "success"})



@app.route('/complaint', methods=['post', 'get'])
def complaint():
    try:
        uid=request.form['uid']
        complt=request.form['complaint']
        cmd.execute("insert into complaint values(NULL,"+uid+",'"+complt+"',curdate(),'pending')")
        con.commit()
        return jsonify({'task':"success"})
    except Exception as e:
        print(str(e))
        return jsonify({'task': "Faild"})

@app.route('/payment', methods=['post', 'get'])
def payment():
    try:
        print(request.form)
        bid=request.form['booking_id']
        slotid=request.form['slot']
        print(slotid)
        amount=request.form['amount']
        cmd.execute("insert into payment values(NULL,"+bid+",'"+amount+"','pending','"+slotid+"')")
        id=con.insert_id()
        con.commit()
        return jsonify({'task':"success","id":str(id)})
    except Exception as e:
        print(str(e))
        return jsonify({'task': "Faild"})












@app.route('/viewslot1',methods=['post'])
def viewslot1():
    cmd.execute("SELECT * FROM slot where status='free'")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()
    json_data = []
    for result in results:
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)

@app.route('/parking_location_add1',methods=['post'])
def parking_location_add1():
    cmd.execute("SELECT * FROM parking_location_registration ")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()
    json_data = []
    for result in results:
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)
@app.route('/viewslot2',methods=['post'])
def viewslot2():
    uid=request.form['uid']
    print(uid)
    sltid = request.form['sltid']
    print(sltid)
    cmd.execute("INSERT into booking values(null,'"+str(uid)+"','"+str(sltid)+"',now(),'confirm')")
    cmd.execute("UPDATE slot SET `status`='booked' WHERE `slot_id`='"+sltid+"'")
    con.commit()


    return jsonify({"task":'success'})

@app.route('/viewpayment', methods=['post'])
def viewpayment():
        u_id = request.form['uid']
        cmd.execute("SELECT payment.*,booking.slot_id,booking.date FROM booking JOIN payment ON booking.booking_id=payment.booking_id WHERE booking.user_id="+u_id+"")
        row_headers = [x[0] for x in cmd.description]
        results = cmd.fetchall()
        json_data = []
        for result in results:
            json_data.append(dict(zip(row_headers, result)))
        con.commit()
        print(results, json_data)
        return jsonify(json_data)
@app.route('/viewcomplaint',methods=['post'])
def viewcomplaint():
    uid=request.form['uid']
    cmd.execute("SELECT * FROM complaint  WHERE user_id='"+str(uid)+"'")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()
    json_data = []
    for result in results:
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)
@app.route('/alertnoti',methods=['post'])
def alertnoti():
    uid=request.form['uid']
    print(uid)
    cmd.execute("SELECT `alert`.* FROM alert JOIN `booking` ON `booking`.`booking_id`=`alert`.`booking_id`  WHERE `booking`.`user_id`='"+str(uid)+"' AND `booking`.status='entered'")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()


    json_data = []
    for result in results:
        cmd.execute("update alert set status='viewed' where booking_id=" + str(result[1]))
        con.commit()
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)
@app.route('/viewslottt',methods=['post'])
def viewslottt():
    uid=request.form['uid']
    print(uid)

    print("SELECT `slot`.slot_id,slot_number,`booking`.`booking_id` FROM `booking` JOIN `slot` ON `slot`.`slot_id`=`booking`.`slot_id` AND `booking`.`user_id`='"+uid+"' WHERE booking.status='free'")
    cmd.execute("SELECT `slot`.slot_id,slot_number,`booking`.`booking_id` FROM `booking` JOIN `slot` ON `slot`.`slot_id`=`booking`.`slot_id` AND `booking`.`user_id`='"+uid+"' WHERE  `booking`.`booking_id` not in (SELECT `booking_id` FROM `payment`)")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()
    json_data = []
    for result in results:
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)


# @app.route('/viewslottt',methods=['post'])
# def viewslottt():
#     uid=request.form['uid']
#     print(uid)
#
#     print("SELECT `slot`.slot_id,slot_number,`booking`.`booking_id` FROM `booking` JOIN `slot` ON `slot`.`slot_id`=`booking`.`slot_id` AND `booking`.`user_id`='"+uid+"' WHERE booking.status='free'")
#     cmd.execute("SELECT `slot`.slot_id,slot_number,`booking`.`booking_id` FROM `booking` JOIN `slot` ON `slot`.`slot_id`=`booking`.`slot_id` AND `booking`.`user_id`='"+uid+"' WHERE booking.status='free'")
#     row_headers = [x[0] for x in cmd.description]
#     results = cmd.fetchall()
#     json_data = []
#     for result in results:
#         json_data.append(dict(zip(row_headers, result)))
#     con.commit()
#     print(results, json_data)
#     return jsonify(json_data)


@app.route('/viewalert',methods=['post'])
def viewalert():
    uid=request.form['uid']
    print(uid)
    cmd.execute("SELECT alert.* FROM alert JOIN booking ON alert.booking_id=booking.booking_id WHERE booking.user_id='"+str(uid)+"'")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()
    json_data = []
    for result in results:
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)

@app.route('/pay',methods=['post'])
def pay():
     print(request.form)
     q1 = request.form['q1']
     q2 = request.form['q2']

     accno = request.form['accno']
     accho = request.form['accho']
     ifsc = request.form['ifsc']
     bank = request.form['bank']
     amount = request.form['amount']
     id = request.form['id']
     print(amount)
     cmd.execute("select * from amount where account_number='"+accno+"' and acc_holder='"+str(accho)+"' and IFSC='"+str(ifsc)+"' and bank='"+str(bank)+"'")
     results = cmd.fetchone()
     if results is None:
         return jsonify({'task':'no acnt'})
     else:
         amt=results[4]

         cmd.execute("SELECT * FROM `amount` WHERE `amount`>'"+str(amount)+"' ")
         s=cmd.fetchone()
         print("s",s)
         if s is  None:
             return jsonify({'task':'insuffcient balance'})
         else:
             am=s[4]
             blce=int(am)-int(amount)
             cmd.execute("update amount set amount='"+str(blce)+"' where account_number='"+accno+"'")
             cmd.execute("UPDATE `payment` SET `status`='finished' WHERE `payment_id`="+str(id)+"")
             cmd.execute(q1)
             cmd.execute(q2)
             con.commit()
             return jsonify({'task':'success'})








@app.route('/viewslot',methods=['post'])
def viewslot():
    uid=request.form['uid']
    cmd.execute("SELECT `slot`.`slot_number`,slot.status FROM `booking` JOIN `slot` ON `slot`.`slot_id`=`booking`.`slot_id` WHERE `booking`.`user_id`='"+str(uid)+"'")
    row_headers = [x[0] for x in cmd.description]
    results = cmd.fetchall()
    json_data = []
    for result in results:
        json_data.append(dict(zip(row_headers, result)))
    con.commit()
    print(results, json_data)
    return jsonify(json_data)


@app.route('/viewslotamt',methods=['post'])
def viewslotamt():
    bid=request.form['bid']
    print("bid",bid)
    cmd.execute("SELECT (HOUR(TIMEDIFF(NOW(),`date`))+1)*5 FROM `booking` WHERE `booking_id`="+bid+"")
    s=cmd.fetchone()
    if s is not None:
        return jsonify({"task":str(s[0])})
    else:
        return jsonify({"task": "0"})

@app.route('/qrscan', methods=['post'])
def qrscan():

        sid = request.form['sid']
        print(sid, "siid")
        uid = request.form['uid']
        print(sid, "siid")
        cmd.execute("select * from  booking where slot_id='"+str(sid)+"' and status='entered' ")
        s=cmd.fetchone()
        if s is not  None:
            qry1="update booking set status='free' where slot_id='" + str(sid) + "' and user_id='" + str(uid) + "'"
            qry2="update slot set status='free' where slot_id='" + str(sid) + "' "
            # cmd.execute("update booking set status='free' where slot_id='" + str(sid) + "' and user_id='" + str(uid) + "'")
            # cmd.execute("update slot set status='free' where slot_id='" + str(sid) + "' ")
            #
            # con.commit()
            return jsonify({'task': "success1","sid":sid,"bid":str(s[0]),"q1":qry1,"q2":qry2})

        else:
            cmd.execute(
                "update booking set date=now(),status='entered' where slot_id='" + str(sid) + "' and user_id='" + str(
                    uid) + "'")
            con.commit()
            return jsonify({'task': "success"})




if __name__=="__main__":
    app.run(host='0.0.0.0',port=5000)
    #app.run(debug=True)