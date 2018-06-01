var express = require('express');
var http = require('http');
var path = require('path');
var mysql = require('mysql');
var cors = require('cors');
var bodyParser = require('body-parser');
var app = express();
var querystring = require('querystring');
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

// 오너가 회원가입할 때 자동으로 쿠폰번호 1,2,3이 Null로 등록되야 편할듯.
// 오너는 최대 두개의 쿠폰만 등록할 수 있음
// 표 형식으로 쿠폰 두개만 입력할 수 있는 칸을 만들면 될듯 -> 아무것도 적지 않으면 널이 자동으로 들어감.

var server = http.createServer(app).listen(app.get('port'),()=>{
    console.log('서버시작');
})

//timeAttack 쿠폰은 우리가 시간을 정해서 시간이 끝나면 자동으로 파기 되게끔 등록하기.

app.get('/userKey',(req,res)=>{ // 디비 안에 있는 쿠폰목록 중 오너가 올리겠다고 한 것만 알려주는 것
    var user_name = req.body.user_name || req.query.user_name;
    console.log('/userKey호출');
    console.log(user_name);
    connection.query('select user_key from user_list where user_name = \''+user_name+'\''
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

});

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

})

app.post('/postStep',(req,res)=>{

})

app.get('/getWeekStep',(req,res)=>{
    var user_key = req.body.user_key || req.query.user_key;
    console.log('/getWeekStep호출');
    connection.query('select ')
})

app.get('/getMonthStep',(req,res)=>{
    var user_key = req.body.user_key || req.query.user_key;
    console.log('/getMonthStep호출');

})

app.get('/recommendCoupon',(req,res)=>{

})

app.use('/',router);