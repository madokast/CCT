import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import numpy as np

fig = plt.figure()
ax = Axes3D(fig)
x = np.linspace(-2, 2, 40)
y = np.linspace(0, 10, 100)
X, Y = np.meshgrid(x, y)
Z = np.sqrt(4 - pow(X, 2))
Z1 = - np.sqrt(4 - pow(X, 2))
plt.title('demo')
plt.xlabel('X')
plt.ylabel('Y')
ax.scatter(X, Y, Z)
ax.scatter(X, Y, Z1)
plt.legend()
plt.show()
