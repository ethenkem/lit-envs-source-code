import click
from . import push
from . import login
from . import choose_project
from . import pull


@click.version_option("0.1.0")
@click.group()
def main_cli():
    pass


main_cli.add_command(push.push, name="push")
main_cli.add_command(login.login, name="login")
main_cli.add_command(choose_project.select_project, name="choose-project")
main_cli.add_command(pull.pull, name="pull")
