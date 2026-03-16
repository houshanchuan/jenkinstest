package hos.srm.srmbusiness.serviceImpl.login;

import hos.srm.srmbusiness.exception.ErrorException;
import hos.srm.srmbusiness.model.dto.MyLoginUser;
import hos.srm.srmbusiness.model.entity.Loginuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Loginuser user = new Loginuser();
        user.setUserName(username);
        //String password=passwordEncoder.encode("123123");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 原始密码
        String rawPassword = "123456";
        // 使用encode方法进行密码加密
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        List<String> permission=new ArrayList<>(Arrays.asList("test","admin"));

        return new MyLoginUser(user,permission);
    }
}
