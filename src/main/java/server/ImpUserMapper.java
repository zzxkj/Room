package server;

import entity.Friend;
import entity.User;
import mapper.UsersMapper;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import tools.MybatisUtil;

import java.util.List;

public class ImpUserMapper{
    public static SqlSession sqlSession = MybatisUtil.getSqlSession();
    public static UsersMapper userMapper = sqlSession.getMapper(UsersMapper.class);
    public static List<User> SelectAll(){
        List<User> lists = userMapper.selectAll();
        return lists;
    }

    public static User CheckLogin(String username){
        User user = userMapper.checkLogin(username);
        return user;
    }

    public static void InsertUser(User user){
        userMapper.insertUser(user);
        sqlSession.commit();
    }
    public static List<String> FindFriends(String name){
        return userMapper.findFriends(name);
    }
    public static Boolean checkUsername(String name){
        List<String> lists = userMapper.checkUsername(name);
        if (lists == null || lists.size() ==0 ) return false;
        else return true;
    }
    public static void InsertFriend(Friend friend){
        userMapper.insertFriend(friend);
        sqlSession.commit();
    }
    public static List<String> FindFriendN(String name){
       List<String> list= userMapper.findFriendN(name);
       return list;
    }
    public static List<String> FindAvater(String name){
        return userMapper.findAvater(name);
    }
    public static void UpdateFriendNToY(String name){
        int n = userMapper.updateFriendNToY(name);
        sqlSession.commit();
    }
    public static void DeleteFriend(){
        userMapper.deleteFriend();
        sqlSession.commit();
    }
    @Test
    public void test(){
        List<String> lists = FindFriends("888");
        for (String s : lists){
            System.out.println(s);
        }
    }
    @Test
    public void test2(){
        checkUsername("9998");
    }
    @Test
    public void test3(){
        UpdateFriendNToY("888");
    }
    @Test
    public void test4(){
        List<String> lists =ImpUserMapper.FindAvater("555");
        System.out.println(lists.size());
        System.out.println(lists.get(0));
    }
    @Test
    public void test5(){
        List<String> lists = ImpUserMapper.userMapper.findFriendN("888");
        System.out.println(lists.size());
        InsertFriend(new Friend("666","888","N"));
        lists = ImpUserMapper.userMapper.findFriendN("888");
        System.out.println(lists.size());
    }
}
