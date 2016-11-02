class CreateBicycles < ActiveRecord::Migration[5.0]
  def change
    create_table :bicycles do |t|
      t.string :biketype
      t.string :brand
      t.string :model
      t.string :serial
      t.text :notes

      t.timestamps
    end
  end
end
