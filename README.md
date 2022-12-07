# The application could be run in 2 modes:

## Console

No application parameters should be specified to use this mode. The console will ask you to enter parameter in this
format
on each line:

```aidl
paramName=paramValue
```

Press enter on an empty line to end it

## File

In file mode application takes expression from files and output results to file.
To use this mode, user should specify input, params and output file's absolute paths as arguments

Example:

```aidl
java -jar messenger.jar --output-file D:\Learning\JavaMentoringProgram\5#TestDrivenDevelopment\files-to-process\output.html --input-file D:\Learning\JavaMentoringProgram\5#TestDrivenDevelopment\files-to-process\input-template.html --params-file D:\Learning\JavaMentoringProgram\5#TestDrivenDevelopment\files-to-process\params.txt
```