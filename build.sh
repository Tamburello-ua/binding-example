#!/usr/bin/env bash

##############################################################################
##
##  Jenkins script for UN*X
##
##############################################################################

#Variables for local build test
#BUILD_URL="http://jenkins:8080/view/Android/job/Android_HIMYNAMEIS"
#BUILD_ID="2016-02-11_14-07-21"
#BUILD_NUMBER="1"
#GIT_BRANCH="develop"

typeset err_code
echo "Build Url: $BUILD_URL"
echo "Build Id: $BUILD_ID"
echo "Build Number: $BUILD_NUMBER"
echo "Git Branch: $GIT_BRANCH"

git submodule update --init --recursive

XML_LOG=$(curl "${BUILD_URL}api/xml?wrapper=changes&xpath=//changeSet//comment")
CHANGE_LOG=$(echo "$XML_LOG" | sed -e "s/<\/comment>//g; s/<comment>//g; s/<\/*changes>//g")
echo 'Recent Changes: '
echo "$CHANGE_LOG"

last_commit=`git log --pretty=format:'%h' -n 1`
branch="$(echo $GIT_BRANCH | sed 's .\{7\}  ')"

chmod +x ./gradlew
./gradlew clean assembleDebug -PbuildNumber=${BUILD_NUMBER} -PchangeLog="${CHANGE_LOG}" assembleRelease -PbuildNumber=${BUILD_NUMBER} -PchangeLog="${CHANGE_LOG}" crashlyticsUploadDistributionRelease
err_code=$?

rm -rf build_results/
mkdir build_results

cd app/build/outputs/apk/

recursive() {
	for i in "$1"/*;do
    	if [ -d "$i" ];then
    		echo "dir: $i"
        	recursive "$i"
    	elif [ -f "$i" ]; then
    		echo "file: $i"

        	build_type='unknown'
			if [[ $i != *"unaligned"* && $i == *".apk" ]]; then		
				if [[ $i == *"debug"* ]]; then
					build_type='debug'
					echo "build_type: debug"
				elif [[ $i == *"release"* ]]; then
					build_type='release'		
					echo "build_type: release"
				else
					build_type='undefined'
					echo "build_type: undefined"
				fi

				if [[ $build_type != *"undefined"* ]]; then
					output="../../../../build_results/truckcalc_${BUILD_NUMBER}_${build_type}_${last_commit}.apk"
					cp "$i" "$output"
				fi
			else
				echo "unaligned file"
			fi
    	fi
 	done
}

recursive .

cd ../../../../build_results

# Copy files to CustomShare
mkdir -p /mnt/customshare/Products/TruckCalc/Android/#${BUILD_NUMBER}
cp ./*.apk /mnt/customshare/Products/TruckCalc/Android/#${BUILD_NUMBER}

# Print result values and links to file
commit_hash=`git log --pretty=format:'%H' -n 1`
echo ' '
echo ' '
echo '-----------------------------------'
echo ' ' | tee -a build-results.txt
echo ' ' | tee -a build-results.txt
echo 'Output Files: ' | tee -a build-results.txt
echo ' ' | tee -a build-results.txt
for f in *.apk; do
	echo "/mnt/customshare/Products/TruckCalc/Android/#${BUILD_NUMBER}/${f}"  | tee -a build-results.txt
	echo ' ' | tee -a build-results.txt
done
echo ' ' | tee -a build-results.txt
echo ' ' | tee -a build-results.txt
echo ' ' | tee -a build-results.txt
echo "Gitlab link: http://gitlab.kharkov.dbbest.com/android/truck-calc/commit/${commit_hash}" | tee -a build-results.txt
echo ' ' | tee -a build-results.txt
echo ' '
echo '-----------------------------------'
echo ' '
echo ' '

exit $err_code