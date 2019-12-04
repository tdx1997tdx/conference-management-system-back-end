#replace.sh 用于将上次构建的结果备份，然后将新的构建结果移动到合适的位置
#!/bin/bash
# 先判断文件是否存在，如果存在，则备份
file="/root/conference_system/sys.jar"
if [ -f "$file" ]
then
   mv /root/conference_system/sys.jar /root/conference_system/old_version/sys.jar.`date +%Y%m%d%H%M%S`
fi
cp /var/lib/jenkins/jobs/conference-management-system-back-end/workspace/target/conference_system-1.0.jar /root/conference_system/sys.jar