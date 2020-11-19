package tools;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;


public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory;
    public static SqlSessionFactory init(){
          Reader reader = null;
           try{
               reader = Resources.getResourceAsReader("mybatis-config.xml");
           }catch (IOException e){
               e.printStackTrace();
           }
           synchronized (MybatisUtil.class){
               if (sqlSessionFactory == null){
                   try {
                       sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                       reader.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
           return sqlSessionFactory;
    }
    public static SqlSession getSqlSession(){
        if(sqlSessionFactory == null)
            init();
        return sqlSessionFactory.openSession();
    }
}
