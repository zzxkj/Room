import entity.User;
import mapper.UsersMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import tools.MybatisUtil;

import java.util.List;

public class UserMapperTest extends MybatisUtil {
    private SqlSession sqlSession = getSqlSession();
    private UsersMapper userMapper = sqlSession.getMapper(UsersMapper.class);
    @Test
    public void testSelectAll(){
        try{
            List<User> lists = userMapper.selectAll();
            for (User user : lists){
                System.out.println(user.toString());
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            sqlSession.close();
        }

    }
    @Test
    public void testCheckLogin(){
        User user = new User();
        user.setUsername("zzxkj");
        user.setPassword("666");
        System.out.println( userMapper.checkLogin("666").toString());
    }

}
