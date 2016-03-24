------------------------------------------------------------------------------------------------------Access right------------------------------------------------------------------------------------------------------------------
#eg ==> chmod u=rwx,g=rx,o=r myfile
chmod options permissions filename
------------------------------------------------------------------------------------------------------Autosys------------------------------------------------------------------------------------------------------------------
#autosys
autorep -J  IB_IT_MDC_1428_MDC_SIT_%Calc%
autorep -q -J IB_IT_MDC_1428_MDC_SIT_MDC_Run_CalcEngine
sendevent -e FORCE_STARTJOB -j IB_IT_MDC_1428_MDC_SIT_MDC_Run_CalcEngine 
-------------------------------------------------------------------------------------------------------text file-----------------------------------------------------------------------------------------------------------------
#display last 500 line of a file
tail -n 500 /var/log/secure
-------------------------------------------------------------------------------------------------------simple Search-----------------------------------------------------------------------------------------------------------------
#display file type 
file  <filename>

#display a command type
type cd 

#search command under $PATH   
which <command>

#search file by /var/lib/mlocate
#updatedb can update /var/lib/mlocate up-to-date
locate -i *CDS*.csv

#search by real file
find 	[PATH] 	[option] 			[action]
				-mtime <[+-]ndays>                                  #+n means taht last modified before n days. 
																	#-n means from now to the n days before. 
																	#n means that just the day n day age                         search by file last modify
				-mmin <[+-]mmins>
				-uid <userid>  #uid in /etc/passed																				 search by file owner
				-gid <groupid> #groupid in /etc/group																			 search by file owner group
				-user <name>																									 search by file owner
				-group <name>																									 search by file owner group
				-nouser
				-nogroup
				-name <filename>																							     search by file name
				-size <[+-]n[c/k]> #+ or - means > or <, c or k means byte or 1024bytes. for example find / -size +50k           search by file size
				-type <fbcdlsp>    #                                                                                             search by file type
				-perm <[-/]mode>   #mode is chmod's parameter, for example -rwsr-xr-x equals 0744; - means access right > mode,  search by access right
				
				

-------------------------------------------------------------------------------------------------------command or history-----------------------------------------------------------------------------------------------------------------
#search command history >500
history | less

#search used command Ctrl+r and input letter for searching
Ctrl+r 

#supply the command
type+Tab

#ps - report a snapshot of the current processes.  ps -ef
#free - Display amount of free and used memory in the system
#top - display Linux tasks
#uptime - Tell how long the system has been running.
------------------------------------------------------------------------------------------------------Hardisk------------------------------------------------------------------------------------------------------------------
#display hardisk by human knowable way
df -h
#display hardisk -a means file ans dir, -s -S -k-m 
du -h



----------------------------------------------------------------------------------------------------shell  script demo-----------------------------------------------------------------------------------------------------
#!/bin/bash

if [ "xxx" == "$1" ]; then
	echo "you input $1"
elif [ "" == "$1" ]; then
	echo "you input nothing"
else
	echo "you input others..."
	exit 1
fi


echo welcome ADTDDST! you need to input 2 parameters
read -p "first: " param1
read -p "second: " param2
echo "you input are ${param1} and ${param2}"
total=$((${param1}+${param2}))
echo "reault is ${total}"

date1=$(date --date='2 days ago' +%Y%m%d)
echo "2 days age is ${date1}"
echo "today is $(date +%Y%m%d)"

ls -lart

s=3
for ((i=1;i<=s;i=i+1))
do
	echo ""
done


lsresult=$(ls -lart)
line=""
linec=0
linei=0
for cell  in ${lsresult}
do

	linei=$((${linei}+1))
	linec=$((${linec}+1))

	if [ "${linei}" == 3 ]; then
		linec=1
	fi

	if [ "${linei}" -ge 3 ]; then
		if [ "${linec}" -lt 9 ]; then
			line="${line} ${cell}"
		else
			echo "manully print line ==> ${line}"
			line=""
			linec=0
		fi
	fi

done




				
				
