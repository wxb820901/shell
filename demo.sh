 


#!/bin/bash
#/sbcimp/dyn/data/mdp/UAT/autosys/extractor/extractor.sh -path /sbcimp/dyn/data/mdp/UAT/autosys/extractor/products/CDS_HC.sql -filename CreditCurves_mdc_exceptions_[YYYYMMDD].txt -d NOW -t T1 -c SCP -system SCP
getPrevWorkingDate()
{
    t_input_date=$1
    
    if [ "${TDAY}" == "T1" ]; then
        if [ "$(date -d "$t_input_date" '+%a')" == "Sun" ]; then
            t_business_date=$(date -d "$t_input_date -2 day" "+%Y%m%d")
        elif [ "$(date -d "$t_input_date" '+%a')" == "Mon" ]; then
            t_business_date=$(date -d "$t_input_date -3 day" "+%Y%m%d")
        else
            t_business_date=$(date -d "$t_input_date -1 day" "+%Y%m%d")
        fi
    else
        t_business_date=$t_input_date
    fi
}

initialize()
{
        BASEDIR=$(dirname $0)
        DELIMITER="|"
        if [ "${INPUT_DATE}" == "NOW" ];
        then
                if [ "${TIMEZONE}" == "UTC" ]; then
                    current_datetime=$(date -u +"%Y%m%d%H%M%S")
                else
                    current_datetime=$(date +"%Y%m%d%H%M%S")
                fi
        else
                current_datetime="${INPUT_DATE}000000"
        fi
        
        current_date=${current_datetime:0:8}
        getPrevWorkingDate ${current_date}
        business_date=$t_business_date
        
        YYYY=${t_business_date:0:4}
        MM=${t_business_date:4:2}
        DD=${t_business_date:6:2}
        HH=${current_datetime:8:2}
        MI=${current_datetime:10:2}
        SS=${current_datetime:12:2}
        if [ "${TIMEZONE}" == "" ]; then
            TIMEZONE=$(date +"%Z")
        fi
        
        dateformat=`echo $FILENAME | awk -F '[' '{print $2}' | awk -F ']' '{print $1}'`
        prefixdateformat=`echo $APPEND_FILE_PREFIX | awk -F '[' '{print $2}' | awk -F ']' '{print $1}'`
        
        if [ "$dateformat" != "" ]; then
                outputdate=`if [[ $dateformat =~ "YYYY" ]]; then echo $dateformat | sed -e "s/YYYY/${YYYY}/"; fi`
                outputdate=`if [[ $outputdate =~ "MM" ]]; then echo $outputdate | sed -e "s/MM/${MM}/"; else echo $outputdate; fi`
                outputdate=`if [[ $outputdate =~ "DD" ]]; then echo $outputdate | sed -e "s/DD/${DD}/"; else echo $outputdate; fi`
                outputdate=`if [[ $outputdate =~ "HH" ]]; then echo $outputdate | sed -e "s/HH/${HH}/"; else echo $outputdate; fi`
                outputdate=`if [[ $outputdate =~ "MI" ]]; then echo $outputdate | sed -e "s/MI/${MI}/"; else echo $outputdate; fi`
                outputdate=`if [[ $outputdate =~ "SS" ]]; then echo $outputdate | sed -e "s/SS/${SS}/"; else echo $outputdate; fi`
                dateformat="\[${dateformat}\]"
        fi
        
        if [ "$prefixdateformat" != "" ]; then
                prefixoutputdate=`if [[ $prefixdateformat =~ "YYYY" ]]; then echo $prefixdateformat | sed -e "s/YYYY/${YYYY}/"; fi`
                prefixoutputdate=`if [[ $prefixoutputdate =~ "MM" ]]; then echo $prefixoutputdate | sed -e "s/MM/${MM}/"; else echo $prefixoutputdate; fi`
                prefixoutputdate=`if [[ $prefixoutputdate =~ "DD" ]]; then echo $prefixoutputdate | sed -e "s/DD/${DD}/"; else echo $prefixoutputdate; fi`
                prefixoutputdate=`if [[ $prefixoutputdate =~ "HH" ]]; then echo $prefixoutputdate | sed -e "s/HH/${HH}/"; else echo $prefixoutputdate; fi`
                prefixoutputdate=`if [[ $prefixoutputdate =~ "MI" ]]; then echo $prefixoutputdate | sed -e "s/MI/${MI}/"; else echo $prefixoutputdate; fi`
                prefixoutputdate=`if [[ $prefixoutputdate =~ "SS" ]]; then echo $prefixoutputdate | sed -e "s/SS/${SS}/"; else echo $prefixoutputdate; fi`
                prefixdateformat="\[${prefixdateformat}\]"
                
                actual_appending_prefix=`echo ${APPEND_FILE_PREFIX} | sed -e "s/$prefixdateformat/$prefixoutputdate/"`
        else
        	actual_appending_prefix=${APPEND_FILE_PREFIX}
        fi

        EXTRACTOR_DATABASE=`cat /sbcimp/dyn/data/mdp/${ENV}/config/PROCESSOR/extractor.properties | grep extractor.database | cut -d '=' -f 2-`        
        EXTRACTOR_USER=`cat /sbcimp/dyn/data/mdp/${ENV}/config/PROCESSOR/credentials.properties | grep persistence.jdbc.username | cut -d '=' -f 2-`
        EXTRACTOR_PASSWORD_ENCRYPTED=`cat /sbcimp/dyn/data/mdp/${ENV}/config/PROCESSOR/credentials.properties | grep persistence.jdbc.password | cut -d '=' -f 2-`

        ENVLOWER=`echo $ENV | tr '[A-Z]' '[a-z]'`
        EXTRACTOR_PASSWORD=`/sbcimp/run/tp/sun/jre/v1.8.0_45/bin/java -cp .:/sbcimp/run/pkgs/mdc/${ENVLOWER}/lib/* com.ubs.mdc.loader.utilities.CipherUtil decode ${EXTRACTOR_PASSWORD_ENCRYPTED}`
        
        UNIQUE_FILENAME=`echo temp.${current_datetime}`
        if [ "$dateformat" != "" ]; then
           outputfile=`echo ${EXTRACTOR_OUTPUT}/${FILENAME} | sed -e "s/$dateformat/$outputdate/"`
        else
           outputfile=`echo ${EXTRACTOR_OUTPUT}/${FILENAME}`
        fi
        sqlfile=${EXTRACTOR_OUTPUT}/${UNIQUE_FILENAME}.sql
        t_outputfile=${EXTRACTOR_OUTPUT}/${UNIQUE_FILENAME}.output
}

appendEOFRecord()
{
        DELIMITER="|"
        no_of_record=`wc -l < $1`
        cksum_raw=`cksum < $1`
        cksum="$( cut -d ' ' -f 1 <<< "$cksum_raw" )"

        echo EOF${DELIMITER}${no_of_record}${DELIMITER}${business_date}${DELIMITER}${TIMEZONE}${DELIMITER}${cksum} >> $1
}

usage()
{
        echo "ERROR: $1"
        echo "ERROR: Usage: $0 -path <SQL_PATH> -filename <FILENAME> -d <date in YYYYMMDD|NOW> -t <T0|T1> -system <SCP|MYSTRO> [Optional: -e <FILE EXTENSION>] [Optional: -z UTC] [Optional: -append <File Prefix>"
        echo "Example 1: e.g. $0 -path ./products/CDS_HC.sql -filename CreditCurves_mdc_exceptions_[YYYYMMDD].txt -d NOW -t T1 -system SCP"
        echo "Example 2: e.g. $0 -path ./products/YC_HC.sql -filename YieldCurves_mdc_exceptions_[YYYYMMDDHHMISS].txt -d 20150112 -t T0 -system MYSTRO"
        echo "Example 2: e.g. $0 -path ./products/YC_HC.sql -filename YieldCurves_mdc_exceptions_[YYYYMMDDHHMISS].txt -d 20150112 -t T0 -system MYSTRO -e .ctl"
        echo "Example 2: e.g. $0 -path ./products/YC_HC.sql -filename YieldCurves_mdc_exceptions_[YYYYMMDDHHMISS].txt -d 20150112 -t T0 -system MYSTRO -z UTC -append YieldCurves_mdc_exceptions_[YYYYMMDD]"
        exit 1
}

renameOutputFile()
{
        mv $1 $2
}

################################################################################
# Main
################################################################################
while [ "$1" != "" ]
do
        case $1 in
                -e*)
                        DONE_FILE=$2
                        export DONE_FILE
                        ;;
                -path*)
                        SQL_PATH="$2"
                        export SQL_PATH
                        ;;
                -filename*)
                        FILENAME="$2"
                        export FILENAME
                        ;;
                -d*)
                        INPUT_DATE="$2"
                        LENGHT_INPUT_DATEITME=`expr length ${INPUT_DATE}`
                        export INPUT_DATE
                        ;;
                -t*)
                        TDAY="$2"
                        export TDAY
                        ;;
                -c*)
                        COPY_MODE="$2"   #optional parameter
                        export COPY_MODE
                        ;;
                -system*)
                        EXTRACTOR_SYSTEM="$2"   #optional parameter
                        export EXTRACTOR_SYSTEM
                        ;;
                -z*)
                        TIMEZONE="$2"   #optional parameter
                        export TIMEZONE
                        ;;
                -append*)
                        APPEND_FILE_PREFIX="$2" #optional parameter
                        export APPEND_FILE_PREFIX
                        ;;
                *)
                        usage "Invalid parameter"
                        ;;
        esac
        shift 2
done

if [ "${SQL_PATH}" == "" ]||[ "${INPUT_DATE}" == "" ]||[ "${TDAY}" == "" ]||[ "${FILENAME}" == "" ]||[ "${EXTRACTOR_SYSTEM}" == "" ]; then
        usage "Insufficent parameters"
elif [ "${LENGHT_INPUT_DATEITME}" -ne 8 ]&&[ "${INPUT_DATE}" != "NOW" ]; then
        usage "Invalid -d parameter, please input either YYYYMMDDHHMMSS or NOW"
elif [ "${TDAY}" != "T0" ]&&[ "${TDAY}" != "T1" ]; then
        usage "Invalid -t parameter, please input either T1 or T0"
fi

initialize
sqlplus -s ${EXTRACTOR_USER}/${EXTRACTOR_PASSWORD}@${EXTRACTOR_DATABASE} @${BASEDIR}/extractor.sql ${SQL_PATH} ${business_date} ${sqlfile} ${t_outputfile} ${DELIMITER}

if [ $? != 0 ]
then
        echo "Extraction failed. Please check below files for more details:"
        echo ${sqlfile}
        echo ${t_outputfile}
        exit 1
else
    if [ "${APPEND_FILE_PREFIX}" != "" ]; then
        #${BASEDIR}/merge.sh ${EXTRACTOR_OUTPUT} ${actual_appending_prefix} ${t_outputfile} ${outputfile}
        ${BASEDIR}/union.sh ${EXTRACTOR_OUTPUT} ${actual_appending_prefix} ${t_outputfile} ${outputfile} EXP_PK  # This has assumed column EXP_PK is always the exception's key
        rm ${t_outputfile}
    else
        renameOutputFile ${t_outputfile} ${outputfile}
    fi
    appendEOFRecord ${outputfile}
    rm ${sqlfile}
    echo "Extraction completed."
fi

# Copy the file to destination
if [ "${COPY_MODE}" == "SCP" ]; then
        ${BASEDIR}/scp.sh ${outputfile} ${EXTRACTOR_SYSTEM} ${DONE_FILE}
elif [ "${COPY_MODE}" == "" ]; then
        echo "File copy is skipped."
fi
