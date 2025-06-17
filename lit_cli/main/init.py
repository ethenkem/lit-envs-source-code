import click
from halo import Halo


@click.command("init")
def init():
    spinner = Halo(text="Initializing...", spinner="dots")
    spinner.start()

    try:
        with open(".gitignore1", "r") as f:
            existing = f.read()

        new_lines = (
            "\n# Added by Lit Env(Don't remove these files)\n.lit_env_data.toml\n"
        )

        with open(".gitignore1", "a") as f:
            f.write(new_lines)

        spinner.succeed("Initialization complete. Lit Env Initiated.")
    except FileNotFoundError:
        spinner.fail(".gitignore1 not found.")
    except Exception as e:
        spinner.fail(f"Error: {e}")
