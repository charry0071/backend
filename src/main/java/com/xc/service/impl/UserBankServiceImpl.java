package com.xc.service.impl;


import com.xc.common.ServerResponse;
import com.xc.dao.UserBankMapper;
import com.xc.pojo.User;
import com.xc.pojo.UserBank;
import com.xc.service.IUserBankService;
import com.xc.service.IUserService;
import com.xc.vo.user.UserBankInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

 @Service("iUserBankService")
 public class UserBankServiceImpl implements IUserBankService {

   @Autowired
   UserBankMapper userBankMapper;

   @Autowired
   IUserService iUserService;

   public UserBank findUserBankByUserId(Integer userId) { return this.userBankMapper.findUserBankByUserId(userId); }

   public ServerResponse addBank(UserBank bank, HttpServletRequest request) {

     User user = this.iUserService.getCurrentUser(request);

     UserBank dbBank = this.userBankMapper.findUserBankByUserId(user.getId());

     if (dbBank != null) {

       return ServerResponse.createByErrorMsg("Thông tin ngân hàng đã có, vui lòng không thêm lại");

     }
     UserBank userBank = new UserBank();

     userBank.setUserId(user.getId());

     userBank.setBankName(bank.getBankName());

     userBank.setBankNo(bank.getBankNo());

     userBank.setBankAddress(bank.getBankAddress());

     userBank.setBankImg(bank.getBankImg());

     userBank.setBankPhone(bank.getBankPhone());

     userBank.setAddTime(new Date());

     int insertCount = this.userBankMapper.insert(userBank);

     if (insertCount > 0) {
       return ServerResponse.createBySuccess("Thành công thêm tài khoản ngân hàng");

     }

     return ServerResponse.createByErrorMsg("Không thể thêm thẻ ngân hàng");

   }

   public ServerResponse updateBank(UserBank bank, HttpServletRequest request) {

     User user = this.iUserService.getCurrentUser(request);

     UserBank dbBank = this.userBankMapper.findUserBankByUserId(user.getId());

     if (dbBank == null) {

       return ServerResponse.createByErrorMsg("Sửa đổi thất bại，找不到银行");

     }

     dbBank.setBankName(bank.getBankName());

     dbBank.setBankNo(bank.getBankNo());

     dbBank.setBankAddress(bank.getBankAddress());

     dbBank.setBankImg(bank.getBankImg());

     dbBank.setBankPhone(bank.getBankPhone());

     int updateCount = this.userBankMapper.updateByPrimaryKeySelective(dbBank);

     if (updateCount > 0) {

       return ServerResponse.createBySuccess("Thành công sửa đổi tài khoản ngân hàng");

     }

     return ServerResponse.createByErrorMsg("Không thể sửa đổi thẻ ngân hàng");
   }

   public ServerResponse getBankInfo(HttpServletRequest request) {

     User user = this.iUserService.getCurrentUser(request);

     UserBank dbBank = this.userBankMapper.findUserBankByUserId(user.getId());

     if (dbBank == null) {

       return ServerResponse.createByErrorMsg("Chưa thêm thông tin ngân hàng");

     }

     UserBankInfoVO userBankInfoVO = new UserBankInfoVO();

     userBankInfoVO.setRealName(user.getRealName());

     userBankInfoVO.setBankName(dbBank.getBankName());

     userBankInfoVO.setBankAddress(dbBank.getBankAddress());

     userBankInfoVO.setBankNo(dbBank.getBankNo());

     return ServerResponse.createBySuccess(userBankInfoVO);

   }

   public ServerResponse updateBankByAdmin(UserBank userBank) {

     if (userBank.getId() == null) {

       return ServerResponse.createByErrorMsg("ID sửa đổi phải được thông qua");

     }

     int updateCount = this.userBankMapper.updateByPrimaryKeySelective(userBank);

     if (updateCount > 0) {

       return ServerResponse.createBySuccessMsg("Sửa đổi thành công");

     }

     return ServerResponse.createByErrorMsg("Sửa đổi thất bại");

   }

   public ServerResponse getBank(Integer userId) { return ServerResponse.createBySuccess(this.userBankMapper.findUserBankByUserId(userId)); }

 }
