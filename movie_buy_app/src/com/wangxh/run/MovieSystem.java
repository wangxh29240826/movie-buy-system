package com.wangxh.run;

import com.wangxh.bean.Business;
import com.wangxh.bean.Customer;
import com.wangxh.bean.Movie;
import com.wangxh.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.GenericArrayType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MovieSystem {
    /**
     * 定义系统的数据容器用户存储数据
     * 1.存储很多用户（客户对象，商家对象）
     */

    public static final List<User> All_USERS = new ArrayList<>();

    /**
     *2.存储系统全部商家和其排片信息
     * 商家1 = [p1,p2,p3----]
     * 商家2 = [p2,p3--]
     * --
     */
    public static Map<Business,List<Movie>> ALL_MOVIES = new HashMap<>();

    //定义数据接收
    public static final Scanner SYS_SC = new Scanner(System.in);

    //定义一个静态User类型的变量记住当前登入成功的用户对象
    public static User loginUser;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final Logger LOGGER = LoggerFactory.getLogger("MovieSystem");


    /**
     * 3.准备一些测试数据
     *
     */

    static {
        Customer c = new Customer();
        c.setLoginName("wxh666");
        c.setPassWord("123456");
        c.setUsername("Ara~追着风跑");
        c.setSex('男');
        c.setMoney(10000);
        c.setPhone("110110");
        All_USERS.add(c);

        Customer c1 = new Customer();
        c1.setLoginName("ywq520");
        c1.setPassWord("123456");
        c1.setUsername("Ara~追着风跑的妹子");
        c1.setSex('女');
        c1.setMoney(2000);
        c1.setPhone("11111");
        All_USERS.add(c1);

        Business b1 = new Business();
        b1.setLoginName("baozhugong888");
        b1.setPassWord("123456");
        b1.setUsername("包租公");
        b1.setSex('男');
        b1.setMoney(0);
        b1.setPhone("110110");
        b1.setShopName("万家电影城");
        All_USERS.add(b1);
        //注意：商家一定要加入到店铺排片中
        List<Movie> movies = new ArrayList<>();
        ALL_MOVIES.put(b1,movies);

        Business b2 = new Business();
        b2.setLoginName("baozhupo888");
        b2.setPassWord("123456");
        b2.setUsername("包租婆");
        b2.setSex('女');
        b2.setMoney(0);
        b2.setPhone("110110");
        b2.setShopName("万达电影城");
        All_USERS.add(b2);
        //注意：商家一定要加入到店铺排片中
        List<Movie> movies3 = new ArrayList<>();
        ALL_MOVIES.put(b2,movies3);
        //2个商家两个客户


    }


    public static void main(String[] args) {
        showMain();
    }

    /**
     * 首页展示
     */
    private static void showMain() {
        System.out.println("================王总电影展示==============");
        System.out.println("1.登入:");
        System.out.println("2.用户注册:");
        System.out.println("3.商家注册:");
        while (true) {
            System.out.println("请输入操作指定:");
            String command = SYS_SC.nextLine();
            switch (command){
                case "1" :
                    //登入
                    login();
                    break;
                case "2" :
                    break;
                case "3" :
                    break;
                default:
                    System.out.println("命令有误请重新输入:");
            }
        }

    }


    /**
     * 登入功能
     */
    private static void login() {
        while (true) {
            System.out.println("请您输入登入名称：");
            String loginName = SYS_SC.nextLine();
            System.out.println("请您输入登入密码：");
            String passWord = SYS_SC.nextLine();

            //1.根据登入名称查询用户对象
            User u = getUserByLoginName(loginName);
            //2,判断用户对象是否存在，存在说明登录名称正确了
            if (u != null){
                //3.比对密码是否正确
                if (u.getPassWord().equals(passWord)){
                    //登录成功了
                    loginUser = u;//记住成功登入的用户
                    LOGGER.info(u.getUsername() + "登入了系统~~~");
                    //判断是用户登录的,还是商家登录
                    if (u instanceof Customer) {
                        //当前登入的是普通用户
                        showCustomerMain();
                    }else{
                        //当前登入的是商家用户
                        showBusinessMain();
                    }

                }else{
                    System.out.println("密码有误啊，呀谁啊累");
                }
            }else{
                System.out.println("登入名称错误，请确认!");
            }
        }
    }

    /**
     * 商家后台操作页面
     */
    private static void showBusinessMain() {
        while (true) {
            System.out.println("==============王总电影商家界面================");
            System.out.println("1.展示详情：");
            System.out.println("2.上架电影：");
            System.out.println("3.下架电影：");
            System.out.println("4.修改电影：");
            System.out.println("5.退出：");
            System.out.println("请输入您要操作的命令：");
            String command = SYS_SC.nextLine();
            switch (command){
                case "1":
                    //展示全部排片信息
                    showBusinessInfos();
                    break;
                case "2":
                    //上架电影信息
                    addMovie();
                    break;
                case "3":
                    // 下架电影信息
                    deleteMovie();
                    break;
                case "4":
                    // 修改电影信息
                    updateMovie();

                    break;
                case "5":
                    System.out.println(loginUser.getUsername() + "请您下次再来~~");
                    return;// 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;

            }
        }
    }

    /**
     * 影片修改功能
     */
    private static void updateMovie() {
        System.out.println("==============修改电影============");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() == 0){
            System.out.println("当前无片可以修改~~");
            return;
        }
        //2.让用户选择需要的修改的电影名称
        while (true) {
            System.out.println("请您输入需要修改的电影名称：");
            String movieName = SYS_SC.nextLine();

            //3.去查询有没有这个影片对象
            Movie movie = getMovieByname(movieName);
            if (movie != null){
                //修改它
                System.out.println("请您修改的新片名：");
                String name = SYS_SC.nextLine();
                System.out.println("请您修改的主演：");
                String actor = SYS_SC.nextLine();
                System.out.println("请您修改的时长：");
                String time = SYS_SC.nextLine();
                System.out.println("请您修改的票价：");
                String price = SYS_SC.nextLine();
                System.out.println("请您修改的票数：");
                String totalNumber = SYS_SC.nextLine();
                while (true) {
                    //public Movie(String name,String actor,double time,double price,int number,Data startTime)
                    try {
                        System.out.println("请您输入修改后的影片放映时间：");
                        String stime = SYS_SC.nextLine();

                        movie.setName(name);
                        movie.setActor(actor);
                        movie.setPrice(Double.valueOf(price));
                        movie.setTime(Double.valueOf(time));
                        movie.setNumber(Integer.valueOf(totalNumber));
                        movie.setStartTime(sdf.parse(stime));
                        showBusinessInfos();
                        System.out.println("感谢您，修改成功!!");
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("时间解析出了毛病~~");
                    }
                }
            }else {
                System.out.println("您的店铺没有上架该影片：");
                System.out.println("请问继续嘛！！请输YesOrNO!");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "Yes" :
                        break;
                    default:
                        System.out.println("好嘞！");
                        return;
                }
            }
        }

    }

    /**
     * 影片下架功能
     */
    private static void deleteMovie() {
        System.out.println("==============下架电影============");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() == 0) {
            System.out.println("当前无电影可以下载!");
            return;
        }

        //2.让用户选择需要的下载的电影名称
        while (true) {
            System.out.println("请您输入需要下架的电影名称：");
            String movieName = SYS_SC.nextLine();

            //3.去查询有没有这个影片对象
            Movie movie = getMovieByname(movieName);
            if (movie != null){
                //下架它
                movies.remove(movie);
                System.out.println("您当前店铺已经成功下架了：" + movie.getName());
                showBusinessInfos();
                return;
            }else {
                System.out.println("您的店铺没有上架该影片");
                System.out.println("请问继续嘛！请输YesOrNO!");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "Yes" :
                        break;
                    default:
                        System.out.println("好嘞！");
                        return;
                }
            }
        }
    }

    /**
     * 去查询当前商家下的排片
     */
    public static Movie getMovieByname(String movieName){
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName().contains(movieName)){
                return movie;
            }
        }
        return null;
    }

    private static void addMovie() {
        System.out.println("==============商家电影=============");
        //根据商家对象(就是登录的用户loginUser)，作为Map集合的键,提取对应的值就是其排片信息：
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);

        System.out.println("请您输入新片名：");
        String name = SYS_SC.nextLine();
        System.out.println("请您输入主演：");
        String actor = SYS_SC.nextLine();
        System.out.println("请您输入时长：");
        String time = SYS_SC.nextLine();
        System.out.println("请您输入票价：");
        String price = SYS_SC.nextLine();
        System.out.println("请您输入票数：");
        String totalNumber = SYS_SC.nextLine();
        while (true) {
            System.out.println("请您输入影片放映时间：");
            String stime = SYS_SC.nextLine();
            //public Movie(String name,String actor,double time,double price,int number,Data startTime)
            try {
                Movie movie = new Movie(name,actor,Double.valueOf(time),Double.valueOf(price)
                        , Integer.valueOf(totalNumber),sdf.parse(stime));
                movies.add(movie);
                System.out.println("您已经成功上架了：（" + movie.getName() + ")");
                return;
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("时间解析出了毛病~~");
            }
        }
    }


    /**
     * 展示商家的详细信息：
     */
    private static void showBusinessInfos() {
        System.out.println("=========商家详情界面==========");
        LOGGER.info(loginUser.getUsername() + "商家，正在看自己的详情~~~");
        //根据商家对象：（就是登入的用户loginUser），作为Map集合前提，提取对应的值，就是其排片信息
        Business business = (Business) loginUser;
        System.out.println(business.getShopName() + "\t\t电话" + business.getPhone() + "\t\t地址" + business.getAddress()
        + business.getAddress() + "\t\t" + business.getMoney());
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() > 0){
            System.out.println("片名\t\t\t主演\t\t时长\t\t评分\t\t票价\t\t余票数量\t\t放映时间");
            for (Movie movie: movies) {

                System.out.println(movie.getName() + "\t\t\t" + business.getPhone() + "\t\t" + movie.getTime()
                        + "\t\t" + movie.getScore() + "\t\t" + movie.getPrice() + "\t\t" + movie.getNumber() + "\t\t"
                        + sdf.format(movie.getStartTime()));
            }
        }else{
            System.out.println("您的店铺当前无篇在放映~~~~~~");
        }

    }

    /**
     * 客户操作页面
     */
    private static void showCustomerMain() {
        while (true){
        System.out.println("===================小易客户登入页面===============");
        System.out.println(loginUser.getUsername()+(loginUser.getSex()=='男'? "男":"女" + "欢迎您进入系统" +
                "\t余额："+loginUser.getMoney()));
        System.out.println("请您选择要操作的功能：");
        System.out.println("1.展示全部影片信息功能：");
        System.out.println("2.根据电影名称查询电影信息：");
        System.out.println("3.评分功能：");
        System.out.println("4.购票功能：");
        System.out.println("5.退出系统：");

                System.out.println("请输入您要操作的命令：");
                String command = SYS_SC.nextLine();
                switch (command){
                case "1":
                    //展示全部排片信息
                    showAllMovie();
                    break;
                case "2":
                    break;
                case "3":
                    // 评分功能

                    break;
                case"4":
                    // 购票功能
                    buyMovie();
                    break;
                case"5":
                    System.out.println("请您下次再来！！");
                    return;// 干掉方法
                default:
                    System.out.println("不存在该命令！！");
                    break;
                }
        }
    }


    /**
     * 用户购票功能
     */
    private static void buyMovie() {
        showAllMovie();
        System.out.println("==========用户购票=========");
        while (true) {
            System.out.println("请您输入需要买票的店：");
            String shopName = SYS_SC.nextLine();
            //1.查询是否存在商家
            Business business = getBusinessByShopName(shopName);
            if (business == null){
                System.out.println("对不起！没有该店铺!");
            }else{
                //2.此商家全部的排片
                List<Movie> movies = ALL_MOVIES.get(business);
                //3.判断是否存在上映电影
                if (movies.size() > 0){
                    //4.开始进行选片购买
                    System.out.println("请您输入购买电影的名称");
                    String movieName = SYS_SC.nextLine();

                    //去当下商家下，查询该电影对象
                    Movie movie  = getMovieByShopAndName(business,movieName);
                    if (movie != null){
                        //开始购买
                        System.out.println("请您输入要购买的电影票数；");
                        String number = SYS_SC.nextLine();
                        int buyNumber = Integer.valueOf(number);
                        //判断电影是否购票
                        if (movie.getNumber() >= buyNumber){
                            //可以购买了
                            //但前需要花费的金额
                            double money = BigDecimal.valueOf(movie.getPrice()).multiply(BigDecimal.valueOf(buyNumber))
                                    .doubleValue();
                            if (loginUser.getMoney() >= money){
                                //终于可以买票了
                                System.out.println("您成功购买了：" + movie.getName() + buyNumber + "张票！总金额是：" + money);
                                //更新自己的金额，更新商家的金额
                                loginUser.setMoney(loginUser.getMoney() - money);
                                business.setMoney(business.getMoney() + money);
                                movie.setNumber(movie.getNumber()-buyNumber);
                                return;
                            }else {
                                //钱不够
                                System.out.println("是否继续买票？YesOrNO");
                                String command = SYS_SC.nextLine();
                                switch (command){
                                    case "Yes":
                                        break;
                                    default:
                                        System.out.println("好嘞");
                                        return;
                                }
                            }
                        }else{
                            //票数不够
                            System.out.println("您当前最多可以购买：" + movie.getNumber());
                            System.out.println("是否继续买票？YesOrNO");
                            String command = SYS_SC.nextLine();
                            switch (command){
                                case "Yes":
                                    break;
                                default:
                                    System.out.println("好嘞");
                                    return;
                            }
                        }
                    }else{
                        System.out.println("电影名称有毛病");
                    }

                }else{
                    System.out.println("该电影院关门了~~");
                    System.out.println("是否继续买票？YesOrNO");
                    String command = SYS_SC.nextLine();
                    switch (command){
                        case "Yes":
                            break;
                        default:
                            System.out.println("好嘞");
                            return;
                    }
                }
            }
        }
    }
    
    public static Movie getMovieByShopAndName(Business business,String name){
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName().equals(name)){
                return movie;
            }
        }
        return null;
    }

    /**
     * 根据商家店铺名称查询商家对象
     */
    public static Business getBusinessByShopName(String shopName){
        Set<Business> businesses = ALL_MOVIES.keySet();
        for (Business business : businesses) {
            if (business.getShopName().equals(shopName)){
                return business;
            }
        }
        return null;
    }

    /**
     * 展示全部商家排片信息
     */
    private static void showAllMovie() {
        System.out.println("==============展示商家全部排片信息============");
        ALL_MOVIES.forEach((business, movies) -> {

            System.out.println(business.getShopName() + "\t\t电话" + business.getPhone() + "\t\t地址" + business.getAddress());
            System.out.println("\t\t\t片名\t\t\t主演\t\t时长\t\t评分\t\t票价\t\t余票数量\t\t放映时间");
            for (Movie movie: movies) {

                System.out.println("\t\t\t"+ movie.getName() + "\t\t\t" + business.getPhone() + "\t\t" + movie.getTime()
                        + "\t\t" + movie.getScore() + "\t\t" + movie.getPrice() + "\t\t" + movie.getNumber() + "\t\t"
                        + sdf.format(movie.getStartTime()));
            }
        });

    }


    public static User getUserByLoginName(String loginName){
        for (User user : All_USERS) {
            //判断这个用户的登录名称是否是我们想要的
            if (user.getLoginName().equals(loginName)){
                return user;
            }
        }
        //查询此用户登录名称
        return null;
    }
}
