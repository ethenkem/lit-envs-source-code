import click
from halo import Halo


@click.command("init")
def init():
    spinner = Halo(text="Initialising...", spinner="dots")
    spinner.start()
    pass
