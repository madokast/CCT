import numpy as np
from scipy.interpolate import griddata
import matplotlib.pyplot as plt

f=open(r'contour.txt','r')
x=[]#创建三个列表用于存放点的数据
y=[]
h=[]
for line in f:
    p=line[:-1]#去掉每行最后的换行符
    a=p.split(',')#因为每行每个数据之间用","隔开，去掉逗号。
    x.append(float(a[0]))
    y.append(float(a[1]))
    h.append(float(a[2]))
f.close()


points=[]
for i in range(len(x)):
    point=[]
    point.append(x[i])
    point.append(y[i])   
    points.append(point)
points=np.array(points)

xi=np.linspace(min(x),max(x),300)
yi=np.linspace(min(y),max(y),300)

xi,yi=np.meshgrid(xi,yi)#网格化
zi=griddata(points,h,(xi,yi),method='cubic')



plt.clabel(plt.contour(xi,yi,zi,[0.3,0.6,0.9,1.2,1.5,1.8]),inline=True,fontsize=10)
plt.axis('equal')
plt.savefig(r'1.pdf')  #把得到的图保存为pdf的格式
