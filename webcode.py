from flask import *
import pyqrcode
from src.dbop import select,selectall,iud

app=Flask(__name__)

@app.route('/')
def home():
    return render_template('login.html')

@app.route('/login',methods=['POST'])
def login():
    uname=request.form['un']
    passwd=request.form['pwd']

    qry="select * from LOGIN where username='"+uname+"' and password='"+passwd+"'"
    print(qry)
    res=select(qry)
    print(res)
    if res is None:
        return '''<script>alert('invalid username or password');window.location='/'</script>'''
    else:
        if res[3]=='admin':
            return render_template('adminhome.html')
        else:
            return '''<script>alert('invalid username or password');window.location='/'</script>'''
@app.route('/admin_view_user')
def admin_view_user():
    qry="select * from user_registration"
    res=selectall(qry)
    print("=============")
    print(res)
    return render_template('adminviewuser.html',val=res)
@app.route('/delete')
def delete():
    user_id=request.args.get('uid')
    qry="DELETE FROM user_registration where login_id="+str(user_id)+""
    iud(qry)
    qry="delete from login where login_id="+str(user_id)+""
    iud(qry)
    return '''<script>alert('deleted');window.location='/admin_view_user'</script>'''

@app.route('/complaint_view')
def complaint_view():
    qry="SELECT complaint.*,user_registration.first_name FROM complaint JOIN user_registration ON complaint.user_id=user_registration.login_id WHERE complaint.reply='pending'"
    res=selectall(qry)
    print("=============")
    print(res)
    return render_template('complaintview.html',val=res)

@app.route('/view_booking')
def view_booking():
    qry="SELECT booking.booking_id, user_registration.first_name ,parking_location_registration.name, slot.slot_number ,booking.date FROM booking JOIN user_registration ON booking.user_id=user_registration.login_id JOIN slot ON booking.slot_id=slot.slot_id JOIN parking_location_registration ON parking_location_registration.parking_id=slot.parking_id WHERE booking.status='confirm' OR booking.status='entered'"
    res=selectall(qry)
    return render_template('viewbooking.html',val=res)
@app.route('/reject')
def reject():
    booking_id = request.args.get('bid')
    qry = "DELETE FROM booking where booking_id=" + str(booking_id) + ""
    iud(qry)
    return '''<script>alert('deleted');window.location='/view_booking'</script>'''
@app.route('/accept')
def accept():
    booking_id = request.args.get('bid')
    qry = "update booking set status='confirm' where booking_id=" + str(booking_id) + ""
    iud(qry)
    return '''<script>alert('confirmd');window.location='/view_booking'</script>'''


@app.route('/reply_complaint',methods=['GET'])
def reply_complaint():
    complaint_id=request.args.get('cid')
    qry = "SELECT complaint.*,user_registration.first_name FROM complaint JOIN user_registration ON complaint.user_id=user_registration.login_id WHERE complaint.complaint_id='"+complaint_id+"'"
    res = select(qry)
    return render_template('replycomplaint.html',val=res)

@app.route('/rsend' ,methods=['POST'])
def rsend():
    reply=request.form['r']
    complaint_id=request.form['cid']
    qry="update complaint set reply='"+reply+"' where complaint_id="+complaint_id+""
    iud(qry)
    return '''<script>alert('reply snd');window.location='/complaint_view'</script>'''


@app.route('/parking_location_add1')
def parking_location_add1():
    qry="select * from parking_location_registration"
    res=selectall(qry)
    print("------------")
    print(res)
    return render_template('parkinglocation_add1.html',val=res)
@app.route('/pdelete')
def pdelete():
    parking_id=request.args.get('pid')
    qry="DELETE FROM parking_location_registration where parking_id="+str(parking_id)+""
    iud(qry)
    return '''<script>alert('deleted');window.location='/parking_location_add1'</script>'''
@app.route('/parking_location_add2',methods=['POST'])
def parking_location_add2():

    return render_template('parkinglocation_add2.html')

@app.route('/save',methods=['POST'])
def save():
    name=request.form['n']
    address=request.form['a']
    place=request.form['p']
    landmark=request.form['l']
    latitude=request.form['lat']
    longitude=request.form['lon']
    qry="insert into parking_location_registration values(null,'"+name+"','"+address+"','"+place+"','"+landmark+"',"+str(latitude)+","+str(longitude)+")"
    iud(qry)
    return '''<script>alert('inserted');window.location='/parking_location_add1'</script>'''

@app.route('/slot_add1')
def slot_add1():
    qry="select * from slot"
    res=selectall(qry)
    print("###")
    print(res)
    qry="SELECT * FROM parking_location_registration"
    res1=selectall(qry)
    return render_template('slot_add1.html',val=res,val1=res1)
@app.route('/slot_add3',methods=['POST'])
def slot_add3():
    pid=request.form['parking location']
    qry="select * from slot where parking_id="+pid+""
    res=selectall(qry)
    print("###")
    print(res)
    qry="SELECT * FROM parking_location_registration"
    res1=selectall(qry)
    return render_template('slot_add1.html',val=res,val1=res1)

@app.route('/sdelete')
def sdelete():
    slot_id=request.args.get('sid')
    qry="DELETE FROM slot where slot_id="+str(slot_id)+""
    iud(qry)
    return '''<script>alert('deleted');window.location='/slot_add1'</script>'''
@app.route('/slot_add2')
def slot_add2():
    qry = "SELECT * FROM parking_location_registration"
    res1 = selectall(qry)

    return render_template('slot_add2.html',val=res1)
@app.route('/ssave',methods=['POST'])
def ssave():
    sno=request.form['sn']
    parking_id=request.form['pl']
    qry = "insert into slot values(null," + str(parking_id) + "," + str(sno) + ",'free')"
    sid = iud(qry)
    big_code = pyqrcode.create(str(sid), error='L', version=27, mode='binary')
    qrs = "./static/qr_code/" + str(sid) + ".png"
    big_code.png(qrs, scale=6, module_color=[0, 0, 0, 128], background=[0xff, 0xff, 0xff])
    return '''<script>alert('inserted');window.location='/slot_add1'</script>'''




app.run(debug=True)