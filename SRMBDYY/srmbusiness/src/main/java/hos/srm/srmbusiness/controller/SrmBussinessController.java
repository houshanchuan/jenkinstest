package hos.srm.srmbusiness.controller;

import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.service.app.BusinessServcie;
import hos.srm.srmbusiness.utils.ReponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srmBussiness")
public class SrmBussinessController {
    @Autowired
    private BusinessServcie businessServcie;
    @RequestMapping(value = "/srmBussiness", method = RequestMethod.POST)
    public ReponseResult srmBussiness(@RequestBody SrmBussinessDto srmBussinessDto) {
        //System.out.println(srmBussinessDto);
        //String res="housc";
        return new ReponseResult<>(200,"成功",businessServcie.sendBusinessData(srmBussinessDto));
    }

}
