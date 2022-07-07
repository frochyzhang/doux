# 输入参数：备份包全名

# 解压备份包
tar -zxvf $1

# 删除有问题的启停脚本和jar包、配置文件
rm -r $HOME/bin/ $HOME/apps

# 移动jar包、配置文件以及启停脚本
mv $HOME/$HOME/* $HOME/