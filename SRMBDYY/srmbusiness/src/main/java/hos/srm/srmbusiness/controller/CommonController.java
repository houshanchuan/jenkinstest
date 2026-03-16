package hos.srm.srmbusiness.controller;

import hos.srm.srmbusiness.model.entity.Loginuser;
import hos.srm.srmbusiness.utils.ReponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("srm/common")
public class CommonController {
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ReponseResult test(@RequestBody Loginuser user){

        return new ReponseResult(200,"success","111");
    }
}
