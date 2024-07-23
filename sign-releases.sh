 #!/usr/bin/bash

root_folder="$(pwd)"
echo "Root folder is $root_folder"
modules=("okhttp-profiler" "okhttp-requests-modifier" "okhttp-requests-modifier-no-op")

for element in "${modules[@]}"; do
    echo "Removing: $element \n"
    rm -rf "releases/$element"
done
echo "Removing DONE"

./gradlew publish --no-build-cache --no-daemon

for element in "${modules[@]}"; do
  echo "Going to $root_folder/releases/$element/io/nerdythings/$element"
  cd "$root_folder/releases/$element/io/nerdythings/$element"
  first_folder=$(ls -d */ | head -n 1)
  version_name="${first_folder%/}"
  cd "$version_name"
  echo "Going to $version_name"

  gpg -ab "$element-$version_name.aar"
  gpg -ab "$element-$version_name.module"
  gpg -ab "$element-$version_name.pom"
  gpg -ab "$element-$version_name-javadoc.jar"
  gpg -ab "$element-$version_name-sources.jar"

  echo "Signing $element-$version_name"

  cd "$root_folder/releases/$element"

  echo "Zipping $element-$version_name.zip"
  zip "$element-$version_name.zip" -r io
done