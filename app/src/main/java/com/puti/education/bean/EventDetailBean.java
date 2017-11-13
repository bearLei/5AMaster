package com.puti.education.bean;

import java.util.List;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 事件详情对象
 *
 * id	问卷id	整数
 number	事件编号	字符串
 rank	事件等级	字符串
 address	事件地址	字符串
 bizTime	事件发生时间	字符串
 type	事件类型	字符串
 status	事件状态	字符串
 participation	参与人数	整数
 desc	事件描述	字符串
 involvedPeople	涉事人信息	数组
 proofTruthAnalysis	真实性分析	对象
 comment	事件评价	对象
 consultExpert	咨询的专家	数组
 proofList	佑证记录	对象
 trackList	跟踪记录	数组
 referDispose	参考处理方法	对象

 involvedPeople对象
 名称	描述	类型	备注
 name	涉事人姓名	字符串
 grade	年级	字符串
 class	班级	字符串
 major	专业	字符串
 dutyRank	责任等级	字符串	“主要责任人”
 actionData	行为数据	json数组
 personProofList	个人佑证记录	json数组
 punish	处罚信息	字符串


 actionData 数组
 名称	描述	类型	备注
 name	行为名称	字符串
 value	行为值	字符串



 personProofList数组
 名称	描述	类型	备注
 title	佑证名称	字符串
 type	做主记录类型	字符串
 address	地点	字符串
 involver	涉事人	字符串
 involverAvatar	涉事人图像	字符串
 time	时间

 comment对象
 名称	描述	类型	备注
 desc	描述	字符串
 commentValue	评价比较	数组

 proofTruthAnalysis对象
 名称	描述	类型	备注
 value	真实度分析值	整数
 desc	真实度分析说明	字符串


 commentValue对象
 名称	描述	类型	备注
 type	类型	字符串
 time	时间	字符串
 value	数据值	数值

 consultExpert数组
 名称	描述	类型	备注
 name	专家名称	字符串
 avatar	专家头像	字符串
 desc	专家描述	字符串
 contact	联系方式	字符串	微信号
 barcode	联系二维码	字符串	微信二维码

 proofList对象
 名称	描述	类型	备注
 textRecord	文字记录	对象
 audioRecord	音频记录	对象
 ImageTextRecord	视频记录	对象

 textRecord对象
 名称	描述	类型	备注
 time	记录时间	字符串
 desc	文字说明	字符串


 audioRecord对象
 名称	描述	类型	备注
 time	记录时间	字符串
 audioList	音频数据列表	数组	[“/audio/111.avi”,” /audio/112.avi”]


 ImageTextRecord对象
 名称	描述	类型	备注
 time	记录时间	对象
 desc	文字说明	对象
 ImageList	图片说明	数组	[“/image/111.png”,” /image/112.png”]

 trackList数组
 名称	描述	类型	备注
 time	跟进时间	字符串
 desc	跟进描述	字符串
 parentFeedback	家长反馈	json对象
 expertFeedback	专家反馈	json对象


 parentFeedback\ expertFeedback对象
 名称	描述	类型	备注
 time	记录时间	对象
 desc	文字说明	对象
 ImageList	图片说明	数组	[“/image/111.png”,” /image/112.png”]

 referDispose对象
 名称	描述	类型	备注
 desc	文字说明	对象
 example	相似案例	数组

 */

public class EventDetailBean {

    public String uid;
    public String time;//	事件发生时间	字符串
    public String address;//	事件地址	字符串
    public int level;       //级别
    public String description;//	事件描述	字符串
    public int type;	//事件类型
    public String typeName;
    public int categories;
    public String categoriesName;

    public UserDetail student;
    public String involvedType;
    public String involvedTypeName;

    public int status;
    public String statusName;

    public String overReason;       //结案词

    public String headTeacherUID;

    //public Comment comment;//评价






}
