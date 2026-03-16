package hos.srm.srmbusiness.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import hos.srm.srmbusiness.model.entity.Loginuser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//使用注解去掉set和get方法 housc
@Data
@NoArgsConstructor
//生成该类下全部属性的构造方法
//@AllArgsConstructor
public class MyLoginUser implements UserDetails
{
    private Loginuser user;
    private List<String> permissions;
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    public  MyLoginUser(Loginuser user,List<String> permissions){
        this.user=user;
        this.permissions=permissions;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //SimpleGrantedAuthority
        if(authorities!=null){
            return authorities;
        }
        //List<SimpleGrantedAuthority> newList=new ArrayList<>();
        if (permissions==null){
            return null;
        }
        return null;
        /*
        for(String permission:permissions)      {
            SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(permission);
            authorities.add(simpleGrantedAuthority);
        }
        //
        //List<SimpleGrantedAuthority> newList= permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;

         */
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
