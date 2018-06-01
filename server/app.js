var express = require('express');
var http = require('http');
var path = require('path');
var mysql = require('mysql');
var cors = require('cors');
var bodyParser = require('body-parser');
var app = express();
var querystring = require('querystring');
var fs = require('fs');
require('date-utils');

app.set('port',process.env.PORT || 3000);

app.use(bodyParser.json());

app.use(cors());
var router = express.Router();

var connection = mysql.createConnection({
    host : 'localhost',
    user : 'root',
    password : 'dbsals123',
    port : '3306',
    database : 'nssu_db'
});

app.use(express.static(__dirname + '/public')); // 보안상 상대방이 접근할 때 바로 app.js로

var server = http.createServer(app).listen(app.get('port'),()=>{
    console.log('서버시작');
})

//timeAttack 쿠폰은 우리가 시간을 정해서 시간이 끝나면 자동으로 파기 되게끔 등록하기.

app.get('/userInformation',(req,res)=>{ // 디비 안에 있는 쿠폰목록 중 오너가 올리겠다고 한 것만 알려주는 것
    var user_name = req.body.user_name || req.query.user_name;
    console.log('/userKey호출');
    console.log(user_name);
    connection.query('select user_key,user_name,user_sex,user_age,user_point from user_list where user_name = \''+user_name+'\''
    ,(err,rows)=>{
        console.log(rows);
        res.json(rows);
    })
});

app.post('/postPoint',function(req,res){
    var user_key = req.body.user_key;
    var point_date = req.body.point_date;
    var point_brand = req.body.point_brand;
    var point_price = req.body.point_price;
    var point_status = req.body.point_status;
    var point_longitude = req.body.point_longitude;
    var point_latitude = req.body.point_latitude;

});

app.get('/getPoint',(req,res)=>{
    var user_key = req.body.user_key || req.query.user_key;
    console.log('/getPoint 호출');
    connection.query('select point_date, point_brand, point_price from usePoint where user_key=\''+user_key+'\''
    ,(err,rows)=>{
        console.log(rows);
        res.json(rows);
    })
})

app.post('/postMyCoupon',(req,res)=>{
    var user_key = req.body.user_key;
    var coupon_key = req.body.coupon_key;
    console.log('/postMyCoupon 호출');
    connection.query('select coupon1,coupon2,coupon3,coupon4,coupon5 from user_list where user_key = \''+user_key+'\''
    ,(err,rows)=>{
        // console.log(rows);
        console.log(rows[0].coupon1);

        if(coupon_key == (rows[0].coupon1) || coupon_key == (rows[0].coupon2) || coupon_key == (rows[0].coupon3) || coupon_key == (rows[0].coupon4) || coupon_key == (rows[0].coupon5)){
            res.send('-2'); // 중복쿠폰 등록
        }
        else if(rows[0].coupon1==0){
            connection.query('update user_list SET coupon1='+coupon_key)
            res.send('update 완료');
        }
        else if(rows[0].coupon2==0){
            connection.query('update user_list SET coupon2='+coupon_key)
            res.send('update 완료');
        }
        else if(rows[0].coupon3==0){
            connection.query('update user_list SET coupon3='+coupon_key)
            res.send('update 완료');
        }
        else if(rows[0].coupon4==0){
            connection.query('update user_list SET coupon4='+coupon_key)
            res.send('update 완료');
        }
        else if(rows[0].coupon5==0){
            connection.query('update user_list SET coupon5='+coupon_key)
            res.send('update 완료');
        }
        else{
            res.send('-1'); // 쿠폰목록 꽉참
        }
    })
});

app.post('/deleteMyCoupon',(req,res)=>{
    var user_key = req.body.user_key;
    var coupon_key = req.body.coupon_key;
    console.log('/deleteMyCoupon 호출');
    connection.query('select coupon1,coupon2,coupon3,coupon4,coupon5 from user_list where user_key = \''+user_key+'\''
    ,(err,rows)=>{
           if(coupon_key == (rows[0].coupon1)){
                connection.query('update user_list SET coupon1=0')
           }
           else if(coupon_key == rows[0].coupon2){
               connection.query('update user_list SET coupon1=0')
           }
           else if(coupon_key == rows[0].coupon3){
               connection.query('update user_list SET coupon1=0')
           }
           else if(coupon_key == rows[0].coupon4){
               connection.query('update user_list SET coupon1=0')
           }
           else if(coupon_key == rows[0].coupon5){
               connection.query('update user_list SET coupon1=0')
           }
           else{
               res.send('-1'); // 그런 쿠폰 없습니다
           }
    })
})

app.get('/getMyCoupon',function(req,res){
    var user_key = req.body.user_key || req.query.user_key;
    console.log('/getMyCoupon 호출');
    connection.query('select coupon_key,coupon_category,coupon_brand,coupon_content,coupon_expired_date from userCouponList where user_key = \''+user_key+'\''
    ,(err,rows)=>{
        console.log(rows);
        res.json(rows);
    })
});

app.get('/allCouponList',(req,res)=>{
    console.log('/allCouponList호출');
    connection.query('select coupon_key,coupon_brand,coupon_category,coupon_content,coupon_expired_date from allCouponList'
    ,(err,rows)=>{
        console.log(rows);
        res.json(rows);
    })
})

app.get('/rankList',(req,res)=>{
    console.log('/rankList 호출');
    var user_key =req.body.user_key || req.query.user_key;
    connection.query('select * from ranking'
        ,(err,rows)=>{
            // res.json(rows);
            var contact = rows;
            // console.log(contact);
            // contact.push("my_name":"\""+user_key+"\"",)
            connection.query('select user_name,step_num,rank_num from ranking where user_key=\''+user_key+'\''
                ,(err,rows)=>{
                    console.log(rows[0].user_name);
                    contact.push({"user_key":user_key,"step_num":rows[0].step_num,"rank_num":rows[0].rank_num,"user_name":rows[0].user_name});
                    res.json(contact);
                })
        })
})

app.post('/postStep',(req,res)=>{

})

app.get('/getWeekStep',(req,res)=>{
    var user_key = req.body.user_key || req.query.user_key;
    var start_date = req.body.start_date || req.query.start_date;
    var finish_date = req.body.finish_date || req.query.finish_date;

    console.log('/getWeekStep 호출');
    connection.query('select month_day,step_num,kcal,km,time from userStatus where (month_day > '+start_date+' and month_day<'+finish_date+') and user_key = \''+user_key+'\''
    ,(err,rows)=>{
        res.json(rows);
    })
})

app.get('/getMonthStep',(req,res)=>{
    // console.log(dd);
    var user_key = req.body.user_key || req.query.user_key;
    var month = req.body.month || req.query.month;
    var finsih_month=parseInt(month)+parseInt(100);
    console.log('/getWeekStep 호출');
    connection.query('select month_day,step_num,kcal,km,time from userStatus where (month_day > '+month+' and month_day<'+finsih_month+') and user_key = \''+user_key+'\''
        ,(err,rows)=>{
            res.json(rows);
        })


})

app.get('/recommendCoupon',(req,res)=>{

})

app.use('/',router);