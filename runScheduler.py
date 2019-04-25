def runScheduler():
  import subprocess

  os.chdir('./algorithm')
  subprocess.call(['javac', '-cp', 'json-20180813.jar', 
                    'Calendar.java', 'JsonUtils.java','Scheduler.java', 'Professor.java', 
                    'StudentGroup.java', 'Classroom.java', 'Subject.java'])
  os.chdir('..')
  subprocess.call(['java', '-cp', 'json-20180813.jar:.', 'Scheduler'])



if "__name__" == "__main__":
  runScheduler()


#javac -cp json-20180813.jar Calendar.java JsonUtils.java Scheduler.java Professor.java StudentGroup.java Classroom.java Subject.java
#java -cp json-20180813.jar:. Scheduler