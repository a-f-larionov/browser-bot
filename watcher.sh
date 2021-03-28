while inotifywait -e modify -r src/*; do
  nohup ./restart.sh &
done
