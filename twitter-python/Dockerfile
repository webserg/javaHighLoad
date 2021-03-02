FROM python:3
RUN apt-get update -y && apt-get install -y build-essential
ADD ./app.py ./app.py
ADD ./requirements.txt ./requirements.txt
RUN pip install -r requirements.txt
CMD ["python","app.py"]