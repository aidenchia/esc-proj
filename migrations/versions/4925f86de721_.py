"""empty message

Revision ID: 4925f86de721
Revises: ebeedfebf42c
Create Date: 2019-04-17 17:51:08.365079

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '4925f86de721'
down_revision = 'ebeedfebf42c'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.alter_column('timetable', 'subject',
               existing_type=sa.VARCHAR(),
               nullable=True)
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.alter_column('timetable', 'subject',
               existing_type=sa.VARCHAR(),
               nullable=False)
    # ### end Alembic commands ###
