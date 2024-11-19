package cn.edu.xmu.oomall.wechatpay.mapper.generator.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivReceiver;

import java.time.LocalDateTime;

@CopyFrom({DivReceiver.class})
public class DivReceiverPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.amount
     *
     * @mbg.generated
     */
    private Integer amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.order_id
     *
     * @mbg.generated
     */
    private String orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.type
     *
     * @mbg.generated
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.account
     *
     * @mbg.generated
     */
    private String account;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.result
     *
     * @mbg.generated
     */
    private String result;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.fail_reason
     *
     * @mbg.generated
     */
    private String failReason;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.create_time
     *
     * @mbg.generated
     */
    private LocalDateTime createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.finish_time
     *
     * @mbg.generated
     */
    private LocalDateTime finishTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_div_receiver.detail_id
     *
     * @mbg.generated
     */
    private String detailId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.id
     *
     * @return the value of wechatpay_div_receiver.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.id
     *
     * @param id the value for wechatpay_div_receiver.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.amount
     *
     * @return the value of wechatpay_div_receiver.amount
     *
     * @mbg.generated
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.amount
     *
     * @param amount the value for wechatpay_div_receiver.amount
     *
     * @mbg.generated
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.description
     *
     * @return the value of wechatpay_div_receiver.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.description
     *
     * @param description the value for wechatpay_div_receiver.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.order_id
     *
     * @return the value of wechatpay_div_receiver.order_id
     *
     * @mbg.generated
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.order_id
     *
     * @param orderId the value for wechatpay_div_receiver.order_id
     *
     * @mbg.generated
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.type
     *
     * @return the value of wechatpay_div_receiver.type
     *
     * @mbg.generated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.type
     *
     * @param type the value for wechatpay_div_receiver.type
     *
     * @mbg.generated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.account
     *
     * @return the value of wechatpay_div_receiver.account
     *
     * @mbg.generated
     */
    public String getAccount() {
        return account;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.account
     *
     * @param account the value for wechatpay_div_receiver.account
     *
     * @mbg.generated
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.result
     *
     * @return the value of wechatpay_div_receiver.result
     *
     * @mbg.generated
     */
    public String getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.result
     *
     * @param result the value for wechatpay_div_receiver.result
     *
     * @mbg.generated
     */
    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.fail_reason
     *
     * @return the value of wechatpay_div_receiver.fail_reason
     *
     * @mbg.generated
     */
    public String getFailReason() {
        return failReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.fail_reason
     *
     * @param failReason the value for wechatpay_div_receiver.fail_reason
     *
     * @mbg.generated
     */
    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.create_time
     *
     * @return the value of wechatpay_div_receiver.create_time
     *
     * @mbg.generated
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.create_time
     *
     * @param createTime the value for wechatpay_div_receiver.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.finish_time
     *
     * @return the value of wechatpay_div_receiver.finish_time
     *
     * @mbg.generated
     */
    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.finish_time
     *
     * @param finishTime the value for wechatpay_div_receiver.finish_time
     *
     * @mbg.generated
     */
    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_div_receiver.detail_id
     *
     * @return the value of wechatpay_div_receiver.detail_id
     *
     * @mbg.generated
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_div_receiver.detail_id
     *
     * @param detailId the value for wechatpay_div_receiver.detail_id
     *
     * @mbg.generated
     */
    public void setDetailId(String detailId) {
        this.detailId = detailId == null ? null : detailId.trim();
    }
}