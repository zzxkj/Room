package mapper;

import entity.Friend;
import entity.User;

import java.util.List;

public interface UsersMapper {
    List<User> selectAll();
    User checkLogin(String username);
    Integer insertUser(User user);
    List<String> findFriends(String username);
    List<String> checkUsername(String username);
    List<String> findFriendN(String username);
    List<String> findAvater(String username);
    Integer insertFriend(Friend friend);
    Integer updateFriendNToY(String username);
    Integer deleteFriend();
}
