#include "myiic.h"
#include "delay.h"
#include "usart.h"
//////////////////////////////////////////////////////////////////////////////////	 
//������ֻ��ѧϰʹ�ã�δ��������ɣ��������������κ���;
//Mini STM32������
//IIC ��������	   
//����ԭ��@ALIENTEK
//������̳:www.openedv.com
//�޸�����:2010/6/10 
//�汾��V1.0
//��Ȩ���У�����ؾ���
//Copyright(C) ����ԭ�� 2009-2019
//All rights reserved
////////////////////////////////////////////////////////////////////////////////// 	  


#define	  SlaveAddress   0x46 //����������IIC�����еĴӵ�ַ,����ALT  ADDRESS��ַ���Ų�ͬ�޸�

//��ʼ��IIC
void IIC_Init(void)
{					     
	GPIO_InitTypeDef GPIO_InitStructure;
	//RCC->APB2ENR|=1<<4;//��ʹ������IO PORTCʱ�� 
	RCC_APB2PeriphClockCmd(	RCC_APB2Periph_GPIOB, ENABLE );	
	   
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_8|GPIO_Pin_9;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP ;   //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOB, &GPIO_InitStructure);
 
	IIC_SCL=1;
	IIC_SDA=1;

}



void SDA_IN(void)
{
	GPIO_InitTypeDef GPIO_InitStructure;
	   
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_9;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING ;   //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOB, &GPIO_InitStructure);
}
void SDA_OUT(void)
{
	GPIO_InitTypeDef GPIO_InitStructure;
	   
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_9;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP ;   //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOB, &GPIO_InitStructure);
}
//����IIC��ʼ�ź�
void IIC_Start(void)
{
	SDA_OUT();     //sda�����
	IIC_SDA=1;	  	  
	IIC_SCL=1;
	delay_us(5);
 	IIC_SDA=0;//START:when CLK is high,DATA change form high to low 
	delay_us(5);
	IIC_SCL=0;//ǯסI2C���ߣ�׼�����ͻ�������� 
}	  
//����IICֹͣ�ź�
void IIC_Stop(void)
{
	SDA_OUT();//sda�����
	IIC_SCL=0;
	IIC_SDA=0;//STOP:when CLK is high DATA change form low to high
 	delay_us(5);
	IIC_SCL=1; 
	IIC_SDA=1;//����I2C���߽����ź�
	delay_us(5);							   	
}
//�ȴ�Ӧ���źŵ���
//����ֵ��1������Ӧ��ʧ��
//        0������Ӧ��ɹ�
u8 IIC_Wait_Ack(void)
{
	u8 ucErrTime=0;
	SDA_IN();      //SDA����Ϊ����  
	IIC_SDA=1;delay_us(1);	   
	IIC_SCL=1;delay_us(1);	 
	while(READ_SDA)
	{
		ucErrTime++;
		if(ucErrTime>250)
		{
			IIC_Stop();
			return 1;
		}
	}
	IIC_SCL=0;//ʱ�����0 	   
	return 0;  
} 
//����ACKӦ��
void IIC_Ack(void)
{
	IIC_SCL=0;
	SDA_OUT();
	IIC_SDA=0;
	delay_us(5);
	IIC_SCL=1;
	delay_us(5);
	IIC_SCL=0;
}
//������ACKӦ��		    
void IIC_NAck(void)
{
	IIC_SCL=0;
	SDA_OUT();
	IIC_SDA=1;
	delay_us(5);
	IIC_SCL=1;
	delay_us(5);
	IIC_SCL=0;
}					 				     
//IIC����һ���ֽ�
//���شӻ�����Ӧ��
//1����Ӧ��
//0����Ӧ��			  
void IIC_Send_Byte(u8 txd)
{                        
    u8 t;   
	SDA_OUT(); 	    
    IIC_SCL=0;//����ʱ�ӿ�ʼ���ݴ���
    for(t=0;t<8;t++)
    {              
        IIC_SDA=(txd&0x80)>>7;
        txd<<=1; 	  
		delay_us(1);   //��TEA5767��������ʱ���Ǳ����
		IIC_SCL=1;
		delay_us(1); 
		IIC_SCL=0;	
		delay_us(1);
    }	 
} 	    
//��1���ֽڣ�ack=1ʱ������ACK��ack=0������nACK   
u8 IIC_Read_Byte(unsigned char ack)
{
	unsigned char i,receive=0;
	SDA_IN();//SDA����Ϊ����
    for(i=0;i<8;i++ )
	{
        IIC_SCL=0; 
        delay_us(1);
		IIC_SCL=1;
        receive<<=1;
        if(READ_SDA)receive++;   
		
		//delay_us(1); 
    }					 
    if (!ack)
        IIC_NAck();//����nACK
    else
        IIC_Ack(); //����ACK   
    return receive;
}



////////////////////////////BH1750

void Single_Write_BH1750(u8 REG_Address)
{
		u8 iicack = 0;
    IIC_Start();                  //��ʼ�ź�
    IIC_Send_Byte(SlaveAddress);   //�����豸��ַ+д�ź�
		iicack = IIC_Wait_Ack();
		if(iicack == 1)
			printf("iic ack fail\r\n");
    IIC_Send_Byte(REG_Address);    //�ڲ��Ĵ�����ַ��
		iicack = IIC_Wait_Ack();
		if(iicack == 1)
			printf("iic ack fail\r\n");
    IIC_Stop();                   //����ֹͣ�ź�
		//printf("Single_Write_BH1750\r\n");
}

//********���ֽڶ�ȡ*****************************************
/*
uchar Single_Read_BH1750(uchar REG_Address)
{  uchar REG_data;
    BH1750_Start();                          //��ʼ�ź�
    BH1750_SendByte(SlaveAddress);           //�����豸��ַ+д�ź�
    BH1750_SendByte(REG_Address);                   //���ʹ洢��Ԫ��ַ����0��ʼ	
    BH1750_Start();                          //��ʼ�ź�
    BH1750_SendByte(SlaveAddress+1);         //�����豸��ַ+���ź�
    REG_data=BH1750_RecvByte();              //�����Ĵ�������
	BH1750_SendACK(1);   
	BH1750_Stop();                           //ֹͣ�ź�
    return REG_data; 
}
*/
//*********************************************************
//
//��������BH1750�ڲ�����
//
//*********************************************************
u16 Multiple_read_BH1750(void)
{   
	  u16 returndata = 0;
		u8 iicack;
    IIC_Start();                          //��ʼ�ź�
    IIC_Send_Byte(SlaveAddress+1);         //�����豸��ַ+���ź�
		iicack = IIC_Wait_Ack();
		if(iicack == 1)
			printf("iic ack fail\r\n");
		/*
	 for (i=0; i<3; i++)                      //������ȡ2����ַ���ݣ��洢��BUF
    {
        //BUF[i] = BH1750_RecvByte();          //BUF[0]�洢0x32��ַ�е�����
        if (i == 3)
        {

          buf[i] = IIC_Read_Byte(0);                //���һ��������Ҫ��NOACK
        }
        else
        {		
          buf[i] = IIC_Read_Byte(1);                //��ӦACK
       }
   }*/
		returndata = IIC_Read_Byte(1); 
		returndata = (returndata << 8) + IIC_Read_Byte(0); 
    IIC_Stop();                          //ֹͣ�ź�

		return returndata;
	 //printf("Multiple_read_BH1750\r\n");
}


//��ʼ��BH1750��������Ҫ��ο�pdf�����޸�****
void Init_BH1750(void)
{
	 IIC_Init();
   Single_Write_BH1750(0x01);  
	 Single_Write_BH1750(0x07);
	 Single_Write_BH1750(0x10);   // H- resolution mode
}





















