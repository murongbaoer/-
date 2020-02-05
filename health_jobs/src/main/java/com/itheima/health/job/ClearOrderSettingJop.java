package com.itheima.health.job;

import com.itheima.health.utils.DateUtils;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

/**
 * @author:Liming
 *@date:2020/2/1 22:53
 */
public class ClearOrderSettingJop {

    public void ClearOrederSetting() throws Exception {
        try {
            String date = DateUtils.parseDate2String(new Date());
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql///day59_itcasthealth","root","ROOT");
            PreparedStatement psmt = connection.prepareStatement("DELETE FROM t_ordersetting WHERE  orderDate<?");
            psmt.setString(1,date);
            int row = psmt.executeUpdate();
            System.out.println("一共删除"+row+"条预约记录");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
