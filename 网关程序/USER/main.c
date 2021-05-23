#include "led.h"
#include "delay.h"
#include "sys.h"
#include "usart.h"
#include "usart3.h"
#include "string.h"
#include "common.h"
#include "stdio.h"
#include "string.h"
#include "timer.h"
#include "adc.h"
#include "dht11.h"

#include <math.h>

#include "mpu6050.h"
#include "inv_mpu.h"
#include "inv_mpu_dmp_motion_driver.h" 


//usmart支持部分
//将收到的AT指令应答数据返回给电脑串口
//mode:0,不清零USART3_RX_STA;
//     1,清零USART3_RX_STA;
void NBIOT_at_response(u8 mode)
{
	if(USART3_RX_STA&0X8000)		//接收到一次数据了
	{ 
		USART3_RX_BUF[USART3_RX_STA&0X7FFF]=0;//添加结束符
		printf("%s",USART3_RX_BUF);	//发送到串口
		if(mode)USART3_RX_STA=0;
	} 
}
//ATK-ESP8266发送命令后,检测接收到的应答
//str:期待的应答结果
//返回值:0,没有得到期待的应答结果
//    其他,期待应答结果的位置(str的位置)
u8* NBIOT_check_cmd(u8 *str)
{
	
	char *strx=0;
	if(USART3_RX_STA&0X8000)		//接收到一次数据了
	{ 
		USART3_RX_BUF[USART3_RX_STA&0X7FFF]=0;//添加结束符
		printf("ack:%s\r\n",(u8*)USART3_RX_BUF);
		strx=strstr((const char*)USART3_RX_BUF,(const char*)str);
	} 
	return (u8*)strx;
}
//向ATK-ESP8266发送命令
//cmd:发送的命令字符串
//ack:期待的应答结果,如果为空,则表示不需要等待应答
//waittime:等待时间(单位:10ms)
//返回值:0,发送成功(得到了期待的应答结果)
//       1,发送失败
u8 NBIOT_send_cmd(char *cmd,u8 *ack,u16 waittime)
{
	u8 res=0; 
	USART3_RX_STA=0;
	u3_printf("%s\r\n",cmd);	//发送命令
	//u3_sendstring((char *)cmd);
	if(ack&&waittime)		//需要等待应答
	{
		while(--waittime)	//等待倒计时
		{
			delay_ms(10);
			if(USART3_RX_STA&0X8000)//接收到期待的应答结果
			{
				if(NBIOT_check_cmd(ack))
				{
					printf("ok ack:%s\r\n",(u8*)ack);
					break;//得到有效数据 
				}
				else
					printf("error ack:%s\r\n",(u8*)ack);
					USART3_RX_STA=0;
			} 
		}
		if(waittime==0)res=1; 
	}
	return res;
} 
//向ATK-ESP8266发送指定数据
//data:发送的数据(不需要添加回车了)
//ack:期待的应答结果,如果为空,则表示不需要等待应答
//waittime:等待时间(单位:10ms)
//返回值:0,发送成功(得到了期待的应答结果)luojian
u8 NBIOT_send_data(u8 *data,u8 *ack,u16 waittime)
{
	u8 res=0; 
	USART3_RX_STA=0;
	u3_printf("%s",data);	//发送命令
	if(ack&&waittime)		//需要等待应答
	{
		while(--waittime)	//等待倒计时
		{
			delay_ms(10);
			if(USART3_RX_STA&0X8000)//接收到期待的应答结果
			{
				if(NBIOT_check_cmd(ack))break;//得到有效数据 
				USART3_RX_STA=0;
			} 
		}
		if(waittime==0)res=1; 
	}
	return res;
}
void NBiot_CoAP_Config(void)
{
		//check the nwk whether register and if not for waiting to register successfully
	while(NBIOT_send_cmd("AT+CEREG?\r\n","+CEREG: 0,1",200));	
  //open module RF 
	while(NBIOT_send_cmd("AT+CGPADDR=1\r\n","+CGPADDR:",500));	
	//config CDP server;in other words,config cloud server ip address and CoAP port
	while(NBIOT_send_cmd("AT+QMTCFG=\"ALIAUTH\",0,\"a1fCnFS7KyA\",\"Car_Sensors\",\"f83fb42ff06368ca27b6d535d3d44cfe\"\r\n","OK",500));	
	//open send data to cloud server successfully ack
	while(NBIOT_send_cmd("AT+QMTOPEN=0,\"iot-as-mqtt.cn-shanghai.aliyuncs.com\",1883\r\n","+QMTOPEN: 0,0",500));	
	while(NBIOT_send_cmd("AT+QMTCONN=0,\"Car_Sensors\"\r\n","+QMTCONN: 0,0,0",500));	
	while(NBIOT_send_cmd("AT+QMTSUB=0,1,\"/a1fCnFS7KyA/Car_Sensors/user/get\",0\r\n","+QMTSUB: 0,1,0,1",500));	
	if(NBIOT_send_cmd("AT+QMTPUB=0,1,1,0,\"/sys/a1fCnFS7KyA/Car_Sensors/thing/event/property/post\",\"{params:{TEMP:0,HUMI:0,MQ2:0,MQ3:0,JDX:0.0,JDY:0.0,JSDX:0.0,JSDY:0.0,JSDZ:0.0}}\"\r\n","OK",500))
	{
		NBiot_CoAP_Config();
	}		
}

#define X_ACCEL_OFFSET -600
#define Y_ACCEL_OFFSET -100
#define Z_ACCEL_OFFSET 2900

 int main(void)
 {	
	double X_Angle;
	double Y_Angle;
	double Z_Angle;
	double X_G;
	double Y_G;
	double Z_G;
	short aacx,aacy,aacz;
	u8 temp;
	u8 humi;
	u32 adc2;
	u8 percent2;
	u32 adc3;
	u8 percent3;
	 
	u8 sbuf1[30];
	u8 sbuf[200];
	u16 rlen=0;
	u16 times = 0;
	delay_init();	    	 //延时函数初始化	
	NVIC_Configuration();// 设置中断优先级分组
	uart_init(9600);	 //串口初始化为9600
	usart3_init(9600);//串口3初始化
	LED_Init();//led灯初始化
	Adc_Init();
	DHT11_Init();
  MPU_Init();					//初始化MPU6050
	LED0 = 0;
	NBiot_CoAP_Config();
	LED0 = 1;
	
	while(1)
	{
		if(USART_RX_STA&0x8000)//判断是否收到NBIOT的下发数据
		{
			rlen=USART_RX_STA&0X7FFF;	//得到本次接收到的数据长度
			USART_RX_BUF[rlen]=0;		//添加结束符    


//			updatecnt++;
//			if(updatecnt >= 2)
//			{
//				updatecnt = 0;
//				if(updateflag == 1)
//				{
//					updateflag = 2;
//					sprintf(sbuf,"AT+QMTPUB=0,1,1,0,\"/sys/a1PwokS0nE6/HZSZ/thing/event/property/post\",\"{params:{temp1:%0.1f,ddl1:%0.1f,ryl1:%0.1f,ph1:%0.1f,zd1:%0.1f}}\"\r\n",
//					((float)temp_data1)/10,EC_value1,*ryval1,PH_Value1,TU_value1);
//					while(NBIOT_send_cmd(sbuf,"OK",500));
//				}
//				else if(updateflag == 2)
//				{
//					updateflag = 1;
//					sprintf(sbuf,"AT+QMTPUB=0,1,1,0,\"/sys/a1PwokS0nE6/HZSZ/thing/event/property/post\",\"{params:{CurrentTemperature:%0.1f,RelativeHumidity:%0.1f,LightLuxValue:%0.1f,PM25Value:%0.1f,SoundDecibelValue:%0.1f}}\"\r\n",
//					((float)temp_data)/10,EC_value,*ryval,PH_Value,TU_value);
//					while(NBIOT_send_cmd(sbuf,"OK",500));
//				}
//			}


			USART_RX_STA = 0;//串口接收计数清零
		}
		if(USART3_RX_STA&0x8000)//判断是否收到NBIOT的下发数据
		{
			rlen=USART3_RX_STA&0X7FFF;	//得到本次接收到的数据长度
			USART3_RX_BUF[rlen]=0;		//添加结束符    
			

			
			//printf("%s\r\n",USART3_RX_BUF);
			USART3_RX_STA = 0;//串口接收计数清零
		}
		times++;
		if(times%200 == 0)
		{
			LED0 =!LED0;
			
			DHT11_Read_Data(&temp,&humi);
			
			adc2 = Get_Adc_Average(ADC_Channel_0,2);
			percent2 = adc2*100*2/4096;
			if(percent2 > 100)
				percent2 = 100;
			
			adc3 = Get_Adc_Average(ADC_Channel_1,2);
			percent3 = adc3*100*2/4096;
			if(percent3 > 100)
				percent3 = 100;
			
			MPU_Get_Accelerometer(&aacx,&aacy,&aacz);	//得到加速度传感器数据

			X_Angle = acos((aacx + X_ACCEL_OFFSET) / 16384.0);
			Y_Angle = acos((aacy + Y_ACCEL_OFFSET) / 16384.0);
					/* 弧度值转换为角度值 */
			X_Angle = X_Angle * 57.29577;
			//if(X_Angle<100)
			//{
			//	X_Angle = 0;
			//}

			Y_Angle = Y_Angle * 57.29577;
			//if(Y_Angle<100)
			//{
			//	Y_Angle = 0;
			//}
			
			
			X_G = aacx / 16384.0;
			Y_G = aacy / 16384.0;
			Z_G = aacz / 16384.0;
			
			sprintf(sbuf,"AT+QMTPUB=0,1,1,0,\"/sys/a1fCnFS7KyA/Car_Sensors/thing/event/property/post\",\"{params:{TEMP:%d,HUMI:%d,MQ2:%d,MQ3:%d,JDX:%lf,JDY:%lf,JSDX:%lf,JSDY:%lf,JSDZ:%lf}}\"\r\n",temp,humi,percent2,percent3,X_Angle,Y_Angle,X_G,Y_G,Z_G);
			if(NBIOT_send_cmd(sbuf,"OK",200))
			{
				NBiot_CoAP_Config();
			}
			printf("temp=%d,humi=%d,mq2=%d,mq3=%d,xa=%lf,ya=%lf,xg=%lf,yg=%lf,zg=%lf\r\n",temp,humi,percent2,percent3,X_Angle,Y_Angle,X_G,Y_G,Z_G);
		}
		delay_ms(10);
  }
}


