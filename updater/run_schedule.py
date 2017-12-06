import datetime
import logging
import os
import schedule
import subprocess
import time
from raven import Client
from raven.conf import setup_logging
from raven.handlers.logging import SentryHandler

logger = logging.getLogger(__name__)
sentry_dsn = os.environ.get('SENTRY_DSN')


if sentry_dsn:
    client = Client(sentry_dsn)
    handler = SentryHandler(client)
    handler.setLevel(logging.ERROR)
    setup_logging(handler)
else:
    logging.basicConfig()


def log_execution(command, *args, **kwargs):
    try:
        result = command(*args, **kwargs)
    except:
        logger.error("execption occurred while using {} with {} and {}".format(command, args, kwargs), exc_info=True)
        raise
    if result:
        print(result)


def update_db_with_org2ogr():
    print('\n')
    print("Starting update_db_with_ogr2ogr ({})".format(datetime.datetime.now()))
    print(50 * '-')
    print('\n')
    log_execution(subprocess.check_output, ['sh', '/cronjobs/ogr2ogr.sh'], env=os.environ)
    print('done')
    print(50 * '#')
    print('\n' * 3)



def update_crossingcheck():
    print('\n')
    print("Starting update_crossingcheck ({})".format(datetime.datetime.now()))
    print(50 * '-')
    print('\n')
    log_execution(subprocess.check_output, ['php', '/cronjobs/crossingcheck_cronjob.php'], env=os.environ)
    print('done')
    print(50 * '#')
    print('\n' * 3)


def update_statistics():
    print('\n')
    print("Starting statistics update ({})".format(datetime.datetime.now()))
    print(50 * '-')
    print('\n')
    log_execution(subprocess.check_output, ['php', '/cronjobs/statistic_cronjob.php'], env=os.environ)
    print('done')
    print(50 * '#')
    print('\n' * 3)


if __name__ == '__main__':
    # run once, scheduled for later run after this
    update_db_with_org2ogr()
    update_crossingcheck()
    update_statistics()
    schedule.every().hour.do(update_db_with_org2ogr)
    schedule.every().hour.do(update_crossingcheck)
    schedule.every().day.at("23:55").do(update_statistics)

    while True:
        schedule.run_pending()
        time.sleep(10)



# For more information see the manual pages of crontab(5) and cron(8)
#
# m h  dom mon dow   command
# MAILTO=geometalab@gmail.com
# 1 * * * * sh /cronjobs/ogr2ogr.sh > /dev/console 2>&1
# 5 * * * * php /cronjobs/crossingcheck_cronjob.php > /dev/console 2>&1
# 55 23 * * * php /cronjobs/statistic_cronjob.php > /dev/console 2>&1

# 31 * * * * sh /cronjobs/ogr2ogr.sh
# 32 * * * * php /cronjobs/statistic_cronjob.php
# 33 * * * * php /cronjobs/crossingcheck_cronjob.php
